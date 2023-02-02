package com.dialupdelta.ui.get_to_sleep

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityAnimationOnLeftBinding

class AnimationOnLeftActivity : BaseActivity() {
    private lateinit var binding:ActivityAnimationOnLeftBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_animation_on_left)
        initUi()
    }

    private fun initUi() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.animationLeftVideo)

        val uri = Uri.parse("android.resource://" + packageName + "/R.raw/" + R.raw.rocket)
        binding.animationLeftVideo.setVideoURI(uri)
        binding.animationLeftVideo.requestFocus()
        binding.animationLeftVideo.start()

//        rocketVideo.setOnCompletionListener(OnCompletionListener {
//            supportFragmentManager.beginTransaction().replace(R.id.framwQts, SleepEnhancer2())
//                .commit()
//        })

    }
}