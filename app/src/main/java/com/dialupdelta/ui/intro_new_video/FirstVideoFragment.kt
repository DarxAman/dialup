package com.dialupdelta.ui.intro_new_video

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dialupdelta.R
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentFirstVideoBinding
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setVisible

class FirstVideoFragment : BaseFragment() {
    lateinit var navController: NavController
    private var binding:FragmentFirstVideoBinding?  = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_first_video, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        hideStatusBar(requireActivity())
        initUI()
    }

    private fun initUI() {

        videoPlay()

        binding?.firstVideo?.setOnCompletionListener {
            binding?.nextBtn?.setVisible()
            binding?.nextBtn?.isEnabled = true
            binding?.firstVideo?.pause()
            binding?.firstVideo?.stopPlayback()
        }


        binding?.nextBtn?.setOnClickListener {
            binding?.firstVideo?.pause()
            binding?.firstVideo?.stopPlayback()
            navController.navigate(R.id.action_firstVideoFragment_to_secondVideoFragment)
        }
    }

    private fun videoPlay() {
        val mediaController = MediaController(requireActivity())
        mediaController.setMediaPlayer(binding?.firstVideo)
        binding?.firstVideo?.setMediaController(mediaController)
        val uri = Uri.parse("android.resource://" + requireActivity().packageName + "/R.raw/" + R.raw.offline_splash);
        binding?.firstVideo?.setVideoURI(uri);
        binding?.firstVideo?.start()
    }
}