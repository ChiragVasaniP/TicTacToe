package com.dac.tictactoe.base

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding

abstract class NavigationActivity<DB : ViewDataBinding, VM : BaseViewModel>(vmClass: Class<VM>) :
    BaseActivity<DB, VM>(vmClass) {


    private fun navigateToActivityWithBackStack(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    protected fun navigateToActivityWithoutBackStack(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finishAffinity()
    }

    private fun navigateToActivityWithFinish(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }

    protected fun navigateWithResultLauncher(
        activityClass: Class<*>,
        function: (ActivityResult?) -> Unit
    ) {
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            function.invoke(it)
        }.launch(Intent(this@NavigationActivity, activityClass))
    }
}