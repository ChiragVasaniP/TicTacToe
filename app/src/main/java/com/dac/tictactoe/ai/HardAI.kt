package com.dac.tictactoe.ai

class HardAI {

    internal var player = 'x'
    internal var opponent = 'o'

    internal inner class Move(var row: Int, var col: Int)

    internal fun isMovesLeft(board: Array<CharArray>): Boolean {
        for (i in 0..2)
            for (j in 0..2)
                if (board[i][j] == '_')
                    return true
        return false
    }

    private fun evaluate(board: Array<CharArray>, symbol: Char): Int {
        var score = 0

        // Check for potential wins or blocks for the player
        for (row in 0 until 3) {
            val rowOccupiedByPlayer = (0 until 3).all { board[row][it] == player }
            if (rowOccupiedByPlayer) {
                score -= 100 // Player has a winning possibility
            }

            val rowOccupiedByOpponent = (0 until 3).all { board[row][it] == opponent }
            if (rowOccupiedByOpponent) {
                score += 100 // AI has a winning possibility
            }
        }

        for (col in 0 until 3) {
            val colOccupiedByPlayer = (0 until 3).all { board[it][col] == player }
            if (colOccupiedByPlayer) {
                score -= 100 // Player has a winning possibility
            }

            val colOccupiedByOpponent = (0 until 3).all { board[it][col] == opponent }
            if (colOccupiedByOpponent) {
                score += 100 // AI has a winning possibility
            }
        }

        val mainDiagonalOccupiedByPlayer = (0 until 3).all { board[it][it] == player }
        if (mainDiagonalOccupiedByPlayer) {
            score -= 100 // Player has a winning possibility
        }

        val mainDiagonalOccupiedByOpponent = (0 until 3).all { board[it][it] == opponent }
        if (mainDiagonalOccupiedByOpponent) {
            score += 100 // AI has a winning possibility
        }

        val antiDiagonalOccupiedByPlayer = (0 until 3).all { board[it][2 - it] == player }
        if (antiDiagonalOccupiedByPlayer) {
            score -= 100 // Player has a winning possibility
        }

        val antiDiagonalOccupiedByOpponent = (0 until 3).all { board[it][2 - it] == opponent }
        if (antiDiagonalOccupiedByOpponent) {
            score += 100 // AI has a winning possibility
        }

        return score
    }

    internal fun minimax(board: Array<CharArray>, depth: Int, isMax: Boolean): Int {
        val score = evaluate(board, player)

        if (score == 100 || score == -100)
            return score

        if (!isMovesLeft(board) || depth == 4) // Increased depth for deeper search
            return 0

        var bestVal = if (isMax) Int.MIN_VALUE else Int.MAX_VALUE

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (board[i][j] == '_') {
                    if (isMax) {
                        board[i][j] = player
                        bestVal = maxOf(bestVal, minimax(board, depth + 1, !isMax))
                    } else {
                        board[i][j] = opponent
                        bestVal = minOf(bestVal, minimax(board, depth + 1, !isMax))
                    }
                    board[i][j] = '_'
                }
            }
        }

        return bestVal
    }

    internal fun findBestMove(board: Array<CharArray>): Int {
        // Check if AI has a winning possibility and prioritize it
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (board[i][j] == '_') {
                    board[i][j] = opponent
                    if (evaluate(board, opponent) == 100) {
                        board[i][j] = '_'
                        return i * 3 + j + 1
                    }
                    board[i][j] = '_'
                }
            }
        }

        // If AI doesn't have a winning possibility, defend against the player
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (board[i][j] == '_') {
                    board[i][j] = player
                    if (evaluate(board, player) == -100) {
                        board[i][j] = '_'
                        return i * 3 + j + 1
                    }
                    board[i][j] = '_'
                }
            }
        }

        // If neither player nor AI has a high chance to win, find the best move for the AI
        var bestVal = Int.MIN_VALUE
        var bestMove = -1

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (board[i][j] == '_') {
                    board[i][j] = player
                    val moveVal = minimax(board, 0, false)
                    board[i][j] = '_'
                    if (moveVal > bestVal) {
                        bestVal = moveVal
                        bestMove = i * 3 + j + 1
                    }
                }
            }
        }

        return bestMove
    }

    internal fun constructBoard(player1: MutableList<Int>, comp: MutableList<Int>): Int {
        val board = Array(3) { CharArray(3) }
        if (player1.isEmpty() && comp.isEmpty()) {
            // If both player1 and comp lists are empty, choose a random position
            val randomPosition = (1..9).random()
            return randomPosition
        }
        for (cellId in 1..9) {
            if (player1.contains(cellId)) {
                when (cellId) {
                    1 -> board[0][0] = 'x'
                    2 -> board[0][1] = 'x'
                    3 -> board[0][2] = 'x'
                    4 -> board[1][0] = 'x'
                    5 -> board[1][1] = 'x'
                    6 -> board[1][2] = 'x'
                    7 -> board[2][0] = 'x'
                    8 -> board[2][1] = 'x'
                    9 -> board[2][2] = 'x'
                }
            } else if (comp.contains(cellId)) {
                when (cellId) {
                    1 -> board[0][0] = 'o'
                    2 -> board[0][1] = 'o'
                    3 -> board[0][2] = 'o'
                    4 -> board[1][0] = 'o'
                    5 -> board[1][1] = 'o'
                    6 -> board[1][2] = 'o'
                    7 -> board[2][0] = 'o'
                    8 -> board[2][1] = 'o'
                    9 -> board[2][2] = 'o'
                }
            } else {
                when (cellId) {
                    1 -> board[0][0] = '_'
                    2 -> board[0][1] = '_'
                    3 -> board[0][2] = '_'
                    4 -> board[1][0] = '_'
                    5 -> board[1][1] = '_'
                    6 -> board[1][2] = '_'
                    7 -> board[2][0] = '_'
                    8 -> board[2][1] = '_'
                    9 -> board[2][2] = '_'
                }
            }
        }
        return findBestMove(board)
    }
}
