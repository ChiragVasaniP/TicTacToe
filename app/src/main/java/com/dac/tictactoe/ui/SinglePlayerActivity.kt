package com.dac.tictactoe.ui

import android.content.Intent
import android.media.MediaPlayer
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.dac.tictactoe.R
import com.dac.tictactoe.ai.EasyAI
import com.dac.tictactoe.ai.HardAI
import com.dac.tictactoe.ai.MediumAI
import com.dac.tictactoe.base.BaseActivity
import com.dac.tictactoe.base.BaseViewModel
import com.dac.tictactoe.databinding.ActivitySinglePlayerBinding
import com.dac.tictactoe.util.PrefManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mehdi.sakout.fancybuttons.BuildConfig
import java.util.Random


class SinglePlayerActivity :
    BaseActivity<ActivitySinglePlayerBinding, BaseViewModel>(BaseViewModel::class.java),
    View.OnClickListener {

    var activePlayer = 1

    val mSharedPref: PrefManager by lazy { PrefManager(this) }
    val mScreenWidth: Float by lazy { resources.displayMetrics.widthPixels.toFloat() }
    val mScreenHeight: Float by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { resources.displayMetrics.heightPixels.toFloat() }
    val mediaPlayer1: MediaPlayer by lazy { MediaPlayer.create(this, R.raw.soundc1) }
    val mediaPlayer2: MediaPlayer by lazy { MediaPlayer.create(this, R.raw.soundc2) }
    val btnList by lazy { mutableListOf(mBinding.btneasy, mBinding.btnmedium, mBinding.btnhard) }

    var mImageViewList = ArrayList<LottieAnimationView>()
    private var mPlayerListFirst = ArrayList<Int>()
    private var mPlayerListSecond = ArrayList<Int>()

    override fun getLayoutResourceId() = R.layout.activity_single_player

    override fun initStart(): Unit = with(mBinding) {
        onClick = this@SinglePlayerActivity
        activePlayer = 1
        btnSelector()
        intitialsecells()
        launch {
            delay(300)
            autoPlay()
        }
    }

    private fun btnSelector() {
        btnList.forEachIndexed { index, button ->
            button.setTextColor(
                ContextCompat.getColor(
                    this@SinglePlayerActivity,
                    R.color.colorBtnDefault
                )
            )
            if (index == getCurrentDiff()) {
                button.setTextColor(
                    ContextCompat.getColor(
                        this@SinglePlayerActivity,
                        R.color.colorBtnSelected
                    )
                )
            }
        }
    }

    override fun onClick(view: View?): Unit = with(mBinding) {
        when (view) {
            btneasy -> {
                mSharedPref.setDifficulty(0)
                btnSelector()
            }

            btnmedium -> {
                mSharedPref.setDifficulty(1)
                btnSelector()
            }

            btnhard -> {
                mSharedPref.setDifficulty(2)
                btnSelector()
            }

            btnRefresh -> {
                for (i in 0..8) {
                    mImageViewList[i].setImageResource(android.R.color.transparent)
                    (mImageViewList[i]).setEnabled(true)
                }
                tvResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0f)
                setMargins(view = tvResult as View, top = 24)
                (parentll as? ViewGroup)?.addView(mBinding.llDiff)
                mSharedPref.setFirstMove(true)
                rlRefresh.visibility = View.GONE
                tvResult.setText("")
                mPlayerListFirst.clear()
                mPlayerListSecond.clear()
                launch {
                    delay(300)
                    autoPlay()
                }
            }

            ivback -> onBackPressedDispatcher.onBackPressed()

            ivshare -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Tic Tac Toe")
                var sAux =
                    "\nPlay Tic Tac Toe on your Android phone. Our new modern version appears in a cool design.\n\n"
                sAux += "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                i.putExtra(Intent.EXTRA_TEXT, sAux)
                startActivity(Intent.createChooser(i, "choose one"))
            }
        }
    }

    private fun intitialsecells() {
        for (i in 0..8) {
            val cell = LottieAnimationView(this)
            cell.setLayoutParams(ViewGroup.LayoutParams(-2, -2))
            cell.setTag(Integer.valueOf(i))
            cell.playAnimation()
            cell.id = 2000 + i

            cell.setOnClickListener { view ->
                if (mSharedPref.getMusic()) mediaPlayer1.start()
                when (view.id) {
                    view.id -> CellCkick(view)
                }
            }
            cell.setClickable(true)
            (mBinding.frame as? ViewGroup)?.addView(cell)
            mImageViewList.add(cell)
        }


        val cellSize = (((mScreenWidth) - dpToPx(56)) / 3.0f).toInt()
        (mBinding.frame as? ViewGroup)?.getLayoutParams()?.width = cellSize * 3
        (mBinding.frame as? ViewGroup)?.getLayoutParams()?.height = cellSize * 3
        var x_pos = 0
        var y_pos = 0
        for (i in 0..8) {
            (mImageViewList[i]).setEnabled(true)
            (mImageViewList[i]).setImageResource(0)
            (mImageViewList[i]).isSoundEffectsEnabled = false
            (mImageViewList[i]).layoutParams.width = cellSize
            (mImageViewList[i]).layoutParams.height = cellSize
            (mImageViewList[i]).x = ((x_pos * cellSize).toFloat())
            (mImageViewList[i]).y = ((y_pos * cellSize).toFloat())
            (mImageViewList[i]).scaleX = 1.0f
            (mImageViewList[i]).scaleY = 1.0f
            x_pos++
            if (x_pos == 3) {
                x_pos = 0
                y_pos++
            }
        }
    }

    private fun CellCkick(v: View) {
        val buSelected = v as LottieAnimationView
        var cellId = 0
        val buSelectedId = buSelected.id
        when (buSelectedId) {
            mImageViewList.get(buSelectedId - 2000).id -> cellId = (buSelectedId - 1999)
        }
        playGame(cellId, buSelected)

    }

    private fun playGame(cellId: Int, buSelected: LottieAnimationView) {
        try {

            if (activePlayer == 1) {
                buSelected.setAnimation(R.raw.lottie_circle)
                buSelected.speed = 1.5f
                buSelected.playAnimation()

                mPlayerListFirst.add(cellId)
                activePlayer = 2
                if (findWinner() != -1) {
                    checkWinner()
                    return
                }
                launch {
                    delay(1500)
                    autoPlay()
                }
            } else if (activePlayer == 2) {
                buSelected.setAnimation(R.raw.lottie_cross)
                buSelected.playAnimation()
                // buSelected.setBackgroundColor(Color.BLUE)
                activePlayer = 1
                mPlayerListSecond.add(cellId)
            }
            buSelected.isEnabled = false
            checkWinner()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun autoPlay() {
        try {
            // Finding empty cells
            val emptyCells = findEmptyCells()
            // Handling first move
            val cellId = if (isFirstMove() && getCurrentDiff() == 1) {
                calculateFirstMove()
            } else {
                // Determining current difficulty
                when (getCurrentDiff()) {
                    0 -> easyAIPlay(emptyCells)
                    1 -> mediumAIPlay()
                    else -> hardAIPlay()
                }
            }
            // Selecting image view based on cellId
            val buSelected = mImageViewList.getOrNull(cellId - 1) ?: mImageViewList.first()
            // Playing music if enabled
            if (mSharedPref.getMusic()) {
                mediaPlayer2.start()
            }
            activePlayer = 2
            playGame(cellId, buSelected)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun findEmptyCells(): ArrayList<Int> {
        val emptyCells = ArrayList<Int>()
        for (intNumber in 1..9) {
            if (!(mPlayerListFirst.contains(intNumber) || mPlayerListSecond.contains(intNumber))) {
                emptyCells.add(intNumber)
            }
        }
        return emptyCells
    }

    private fun isFirstMove(): Boolean {
        return mSharedPref.isFirstMove()
    }


    private fun calculateFirstMove(): Int {
        var cellId = Random().nextInt(15) + 1
        if (cellId > 9) {
            cellId = 5
        }
        mSharedPref.setFirstMove(false)
        return cellId
    }

    private fun easyAIPlay(emptyCells: ArrayList<Int>): Int {
        val easyAI = EasyAI()
        return emptyCells[easyAI.Easy(emptyCells.size)]
    }

    private fun mediumAIPlay(): Int {
        val board = createBoard()
        val mediumAI = MediumAI(board, this)
        return mediumAI.getBestMove() + 1
    }

    private fun hardAIPlay(): Int {
        val hardAI = HardAI()
        return hardAI.constructBoard(mPlayerListFirst, mPlayerListSecond)
    }

    private fun createBoard(): CharArray {
        val board = CharArray(9)
        for (i in mPlayerListFirst.indices) {
            board[mPlayerListFirst[i] - 1] = 'X'
        }
        for (i in mPlayerListSecond.indices) {
            board[mPlayerListSecond[i] - 1] = 'O'
        }
        for (i in 0 until 9) {
            if (board[i] != 'X' && board[i] != 'O') {
                board[i] = '0'
            }
        }
        return board
    }


    private fun getCurrentDiff() = mSharedPref.getDifficulty()

    private fun findWinner(): Int {
        val winningConditions = listOf(
            listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9), // Rows
            listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9), // Columns
            listOf(1, 5, 9), listOf(3, 5, 7) // Diagonals
        )
        for (condition in winningConditions) {
            if (condition.all { mPlayerListFirst.contains(it) }) return 1
            if (condition.all { mPlayerListSecond.contains(it) }) return 2
        }
        return -1
    }

    private fun checkWinner() = with(mBinding) {
        val winner = findWinner()

        if (winner != -1) {
            for (i in 0..8) {
                (mImageViewList.get(i)).setEnabled(false)
            }
            redundant()
            val winnerText = if (winner == 1) "Cross wins !" else "Circle wins !"
            tvResult.text = winnerText
            (mBinding.llDiff.getParent() as? ViewGroup)?.removeView(mBinding.llDiff)

            if (mSharedPref.getMusic()) MediaPlayer.create(
                this@SinglePlayerActivity,
                R.raw.soundend
            ).start()
            mPlayerListFirst.clear()
            mPlayerListSecond.clear()
            activePlayer = 1
            var x: Int = mSharedPref.getGamesPlayed()
            x = x + 1
            mSharedPref.setGamesPlayed(x)
        } else {
            //Its a draw
            val emptyCells = ArrayList<Int>()
            for (cellId in 1..9) {
                if (!(mPlayerListFirst.contains(cellId) || mPlayerListSecond.contains(cellId))) {
                    emptyCells.add(cellId)
                }
            }
            if (emptyCells.size.equals(0)) {
                (mBinding.llDiff.getParent() as ViewGroup).removeView(mBinding.llDiff)
                tvResult.setText("Draw !")
                val mediaPlayer: MediaPlayer =
                    MediaPlayer.create(this@SinglePlayerActivity, R.raw.soundend)
                if (mSharedPref.getMusic()) mediaPlayer.start()
                redundant()
                mPlayerListFirst.clear()
                mPlayerListSecond.clear()
                activePlayer = 1
                var x: Int = mSharedPref.getGamesPlayed()
                x = x + 1
                mSharedPref.setGamesPlayed(x)
            }
        }

    }

    private fun dpToPx(dp: Int): Float {
        val displayMetrics = getResources().getDisplayMetrics()
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat()
    }

    private fun redundant() = with(mBinding) {
        tvResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        setMargins(view = tvResult as View, top = 60)
        rlRefresh.visibility = View.VISIBLE
    }


    private fun setMargins(view: View, top: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as? ViewGroup.MarginLayoutParams
            p?.setMargins(0, top, 0, 0)
            view.requestLayout()
        }
    }
}
