package com.dialupdelta.ui.journal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentJournalBinding
import com.dialupdelta.ui.transition.TransitionActivity
import com.dialupdelta.utils.hideStatusBar

class JournalFragment : BaseFragment() {
    private lateinit var binding: FragmentJournalBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        hideStatusBar(requireActivity())
//        val animation = TranslateAnimation(010f, 0f, 50f, 30f)
//        animation.duration = 2000
//        animation.fillAfter = true
//        binding.rockettop.animation = animation
//        binding.rockettop.animate().x(0f).y(-20f).translationYBy(-20f).duration = 1000
//        binding.clRocketBlast.visibility = View.INVISIBLE
//        binding.vidsRocketBlast.visibility = View.VISIBLE
//        val mediaController = MediaController(requireActivity())
//        mediaController.setAnchorView(binding.vidMainRocket)

//        val uri = Uri.parse("android.resource://" + context?.packageName + "/R.raw/" + R.raw.rocketblast)
//        binding.vidsRocketBlast.setVideoURI(uri)
//        binding.vidsRocketBlast.requestFocus()
//        binding.vidsRocketBlast.start()
//        Handler(Looper.getMainLooper()).postDelayed({
//
//            val mediaController1 = MediaController(requireActivity())
//            mediaController1.setAnchorView(binding.vidsRocketBlast)
//            val uri1 = Uri.parse("android.resource://" + context?.packageName + "/R.raw/" + R.raw.rocketmp)
//            binding.vidsRocketBlast.setVideoURI(uri1)
//            binding.vidsRocketBlast.requestFocus()
//            binding.vidsRocketBlast.start()
//        }, 16000)
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(context, TransitionActivity::class.java))
//            binding.vidsRocketBlast.stopPlayback()
//        }, 19000)

        binding.rocketmiddle.setOnClickListener {
//            val animation = TranslateAnimation(010f, 0f, 0f, 50f)
//            animation.duration = 2000
//            animation.fillAfter = true
//            binding.rocketmiddle.animation = animation
            startActivity(Intent(context, JournalActivity::class.java))
           // Handler(Looper.getMainLooper()).postDelayed({}, 2000)
        }

          binding.rockettop.setOnClickListener {
            binding.vidMainRocket.visibility = View.VISIBLE
            val mediaController = MediaController(requireActivity())
            mediaController.setAnchorView(binding.vidMainRocket)

            val uri = Uri.parse("android.resource://" + context?.packageName + "/R.raw/" + R.raw.rocketmid)
            binding.vidMainRocket.setVideoURI(uri)
            binding.vidMainRocket.requestFocus()
            binding.vidMainRocket.start()
            val animation1 = TranslateAnimation(010f, 0f, 50f, 0f)
            animation1.duration = 2000
            animation1.fillAfter = true
            binding.rockettop.animation = animation1
            val animation0 = TranslateAnimation(-50f, 0f, 50f, 0f)
            animation1.duration = 2000
            animation1.fillAfter = true
            binding.vidMainRocket.animation = animation0
            Handler(Looper.getMainLooper()).postDelayed(
                { binding.vidMainRocket.visibility = View.INVISIBLE },
                10000
            )
        }

        binding.rocketbottom.setOnClickListener {
            val animation2 = TranslateAnimation(010f, 0f, 0f, 50f)
            animation2.duration = 2000
            animation2.fillAfter = true
            binding.rocketbottom.animation = animation2
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(context, SurveyActivity::class.java)) }, 3000)
        }
    }
}