package com.dac.tictactoe.ui.intro

import android.os.Parcelable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dac.tictactoe.R
import com.dac.tictactoe.base.BaseViewModel
import com.dac.tictactoe.base.NavigationActivity
import com.dac.tictactoe.databinding.ActivityIntroBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.parcelize.Parcelize

class IntroActivity :
    NavigationActivity<ActivityIntroBinding, BaseViewModel>(BaseViewModel::class.java) {
    override fun getLayoutResourceId() = R.layout.activity_intro

    val dataList = listOf(
        IntroData(R.drawable.ic_logo, "Tic-Tac-Toe", "Play with your friends."),
        IntroData(R.drawable.ic_logo, "Tic-Tac-Toe", "Play with your friends."),
        IntroData(R.drawable.ic_logo, "Tic-Tac-Toe", "Play with your friends.")
    )


    override fun initStart(): Unit = with(mBinding) {
        vpIntro.adapter = ScreenSlidePagerAdapter(this@IntroActivity, dataList = dataList)

        TabLayoutMediator(intoTabLayout, vpIntro) { tab, position ->
        }.attach()
    }


    private inner class ScreenSlidePagerAdapter(
        fa: FragmentActivity,
        private val dataList: List<IntroData>
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = dataList?.size ?: 0



        override fun createFragment(position: Int): Fragment = ScreenSlidePageFragment.newInstance(
            dataList.get(position)
        )
    }

    @Parcelize
    data class IntroData(val image: Int, val title: String, val description: String):Parcelable

}

