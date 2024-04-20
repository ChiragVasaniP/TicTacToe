package com.dac.tictactoe

import android.app.Application
import com.dac.tictactoe.database.ObjectBox

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}