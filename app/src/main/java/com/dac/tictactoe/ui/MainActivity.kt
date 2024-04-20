package com.dac.tictactoe.ui

import android.content.Intent
import android.view.animation.AnimationUtils
import com.dac.tictactoe.R
import com.dac.tictactoe.base.BaseActivity
import com.dac.tictactoe.base.BaseViewModel
import com.dac.tictactoe.database.MyBoxes
import com.dac.tictactoe.databinding.MainScreenBinding
import com.dac.tictactoe.dialog.InfoDialog
import com.dac.tictactoe.dialog.SettingsDialog
import com.dac.tictactoe.util.PrefManager


class MainActivity : BaseActivity<MainScreenBinding, BaseViewModel>(BaseViewModel::class.java) {
    val sharedPef: PrefManager by lazy { PrefManager(this) }

    override fun getLayoutResourceId() = R.layout.main_screen

    override fun initStart():Unit = with(mBinding) {
        MyBoxes.homeData()

        ll1.animation =
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.abc_grow_fade_in_from_bottom)
        ll2.animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.downtoup)

        gamesplayed.text = sharedPef.getGamesPlayed().toString()

        btnSingle.setOnClickListener {

            sharedPef.setFirstMove(true)
            val intent = Intent(this@MainActivity, SinglePlayerActivity::class.java)
            startActivity(intent)
        }
        btnMulti.setOnClickListener {

            sharedPef.setFirstMove(true)
            val intent = Intent(this@MainActivity, TwoPlayerActivity::class.java)
            startActivity(intent)
        }
        ivInfo.setOnClickListener {
            val infoDialog =
                InfoDialog(this@MainActivity)
            infoDialog.show()
        }
        ivSettings.setOnClickListener {
            val settingsDialog = SettingsDialog(this@MainActivity)
            settingsDialog.show()
        }

    }
}

