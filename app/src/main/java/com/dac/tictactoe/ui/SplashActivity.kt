package com.dac.tictactoe.ui

import android.annotation.SuppressLint
import com.dac.tictactoe.R
import com.dac.tictactoe.base.BaseViewModel
import com.dac.tictactoe.base.NavigationActivity
import com.dac.tictactoe.databinding.ActivitySplashBinding
import com.dac.tictactoe.ui.intro.IntroActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity :
    NavigationActivity<ActivitySplashBinding, BaseViewModel>(BaseViewModel::class.java) {

    override fun getLayoutResourceId() = R.layout.activity_splash

    override fun initStart() {
        launch {
            delay(1500)
            navigateToActivityWithoutBackStack(IntroActivity::class.java)
        }

    }
}