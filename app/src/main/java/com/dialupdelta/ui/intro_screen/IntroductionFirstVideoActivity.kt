package com.dialupdelta.ui.intro_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.data.network.response.intro_video_response.IntroVideo
import com.dialupdelta.databinding.ActivityFirstIntroductionVideoBinding
import com.dialupdelta.ui.get_start_activity.GetStartViewModel
import com.dialupdelta.ui.get_start_activity.GetStartViewModelFactory
import com.dialupdelta.ui.intro_new_video.IntroductionVideoActivity
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setVisible
import org.kodein.di.generic.instance

class IntroductionFirstVideoActivity : BaseActivity() {
    private var binding:ActivityFirstIntroductionVideoBinding? = null
    private val factory: GetStartViewModelFactory by instance()
    private lateinit var allVideoListUrl: IntroVideo
    private lateinit var viewModel: GetStartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_first_introduction_video)
        hideStatusBar(this)
        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[GetStartViewModel::class.java]
        viewModel.getIntroductionVideoApi()
        setObserver(viewModel)
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
                it.putExtra(MyKeys.introVideoData, getVideoListUrl())
                startActivity(it)
            }
        }
    }

    private fun setObserver(viewModel: GetStartViewModel) {
        viewModel.introVideoData.observe(this){
            allVideoListUrl = it
        }
    }

    private fun getVideoListUrl():IntroVideo {
        return allVideoListUrl
    }
}