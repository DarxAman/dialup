package com.dialupdelta.ui.intro_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityFirstIntroductionVideoBinding
import com.dialupdelta.ui.intro_new_video.IntroductionVideoActivity
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setVisible

class IntroductionFirstVideoActivity : BaseActivity() {
    private var binding:ActivityFirstIntroductionVideoBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_first_introduction_video)
        hideStatusBar(this)
        initUI()
    }

    private fun initUI() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding?.videoIntroduction)
        val uri = Uri.parse("android.resource://" + packageName + "/R.raw/" + R.raw.introductoryvideo)

        binding?.videoIntroduction?.setVideoURI(uri)
        binding?.videoIntroduction?.requestFocus()
        binding?.videoIntroduction?.start()
        val duration = binding?.videoIntroduction?.duration

        binding?.videoIntroduction?.setOnCompletionListener {
            binding?.nextScreenPlay?.setVisible()
        }

        binding?.nextScreenPlay?.setOnClickListener {
            Intent(this, IntroductionVideoActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}