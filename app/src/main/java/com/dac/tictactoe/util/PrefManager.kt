package com.dac.tictactoe.util

import android.content.Context
import android.content.SharedPreferences
import mehdi.sakout.fancybuttons.BuildConfig

class PrefManager(private val context: Context) {

    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy {
        pref.edit()
    }

    // Shared preferences file name
    private val PREF_NAME = BuildConfig.APPLICATION_ID
    private val IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH"
    private val FIRST_MOVE = "First_Move"
    private val NO_OF_GAMES_PLAYED = "No_Of_Games_Played"
    private val SET_DIFFICULTY = "Set_Difficulty"
    private val MUSIC = "Music"


    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime).apply()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }

    fun setGamesPlayed(gamesPlayed: Int) {
        editor.putInt(NO_OF_GAMES_PLAYED, gamesPlayed).apply()
    }

    fun getGamesPlayed(): Int {
        return pref.getInt(NO_OF_GAMES_PLAYED, 0)
    }

    fun setDifficulty(difficulty: Int) {
        editor.putInt(SET_DIFFICULTY, difficulty).apply()
    }

    fun getDifficulty(): Int {
        return pref.getInt(SET_DIFFICULTY, 1)
    }

    fun setFirstMove(firstMove: Boolean) {
        editor.putBoolean(FIRST_MOVE, firstMove).apply()
    }

    fun isFirstMove(): Boolean {
        return pref.getBoolean(FIRST_MOVE, true)
    }

    fun setMusic(music: Boolean) {
        editor.putBoolean(MUSIC, music).apply()
    }

    fun getMusic(): Boolean {
        return pref.getBoolean(MUSIC, true)
    }
}
