package com.dac.tictactoe.ui.intro

import android.os.Parcelable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dac.tictactoe.R
import com.dac.tictactoe.base.BaseViewModel
import com.dac.tictactoe.base.NavigationActivity
import com.dac.tictactoe.databinding.ActivityIntroBinding
import com.dac.tictactoe.ui.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.parcelize.Parcelize

class IntroActivity :
    NavigationActivity<ActivityIntroBinding, BaseViewModel>(BaseViewModel::class.java),
    View.OnClickListener {
    override fun getLayoutResourceId() = R.layout.activity_intro

    val dataList = listOf(
        IntroData(
            image = R.raw.lottie_idea,
            title = "Welcome",
            description = "Most fun game now available on your smartphone device!"
        ),
        IntroData(
            image = R.raw.lottie_sword,
            title = "Compete",
            description = "Play offline with your friends and enjoy!"
        ),
        IntroData(
            image = R.raw.lottie_winner,
            title = "Scoreboard",
            description = "Welcome to the game! Get ready to embark on an exciting journey to the top of the scoreboard. Earn points in each game and watch yourself climb higher and higher. Let the games begin."
        )
    )


    override fun initStart(): Unit = with(mBinding) {
        vpIntro.adapter = ScreenSlidePagerAdapter(this@IntroActivity, dataList = dataList)
        onClick = this@IntroActivity
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
    data class IntroData(val image: Int, val title: String, val description: String) : Parcelable

    override fun onClick(view: View?): Unit = with(mBinding) {
        when (view) {

            txtBack -> {
                vpIntro.currentItem -= 1
                txtBack.isVisible = vpIntro.currentItem != 0
                txtNext.isVisible = vpIntro.currentItem != dataList.size - 1

            }

            txtNext -> {
                if (vpIntro.currentItem == dataList.size - 1) {
                    navigateToActivityWithoutBackStack(MainActivity::class.java)
                }
                vpIntro.currentItem += 1
                if (vpIntro.currentItem == dataList.size - 1) txtNext.text = "Finish"
                txtBack.isVisible = vpIntro.currentItem != 0
            }

        }
    }

}

