package com.dac.tictactoe.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.dac.tictactoe.R

private const val INTRO_DATA = "Data"


class ScreenSlidePageFragment : Fragment() {
    private var param1: IntroActivity.IntroData? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(INTRO_DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen_slide_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        param1?.image?.let { view.findViewById<LottieAnimationView>(R.id.iv_intro).setAnimation(it) }
        param1?.description?.let { view.findViewById<TextView>(R.id.txt_description).text = it }
        param1?.title?.let { view.findViewById<TextView>(R.id.txt_title).text = it }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: IntroActivity.IntroData) =
            ScreenSlidePageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(INTRO_DATA, param1)
                }
            }
    }
}