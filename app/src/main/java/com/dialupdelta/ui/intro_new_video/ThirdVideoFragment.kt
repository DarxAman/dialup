package com.dialupdelta.ui.intro_new_video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dialupdelta.R
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentThirdVideoBinding
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setVisible
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class ThirdVideoFragment : BaseFragment() {
    private var binding:FragmentThirdVideoBinding? = null
    lateinit var navController: NavController
    var simpleExoPlayer: SimpleExoPlayer? = null
    var newUrl: String = "https://app.whyuru.com/assets/screen_video/screen_3.mp4"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_third_video, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        hideStatusBar(requireActivity())
        initUI()
    }

    private fun initUI() {

        playVideoClick()

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    binding?.nextBtn?.setVisible()
                }
            }
        })

        binding?.nextBtn?.setOnClickListener {
            binding?.playerView?.onPause()
            simpleExoPlayer?.pause()
            simpleExoPlayer?.stop()
            navController.navigate(R.id.action_thirdVideoFragment_to_fourVideoFragment)
        }
    }

    private fun playVideoClick() {
        val mediaItem: MediaItem = newUrl.let { MediaItem.fromUri(it) }
        simpleExoPlayer = SimpleExoPlayer.Builder(requireActivity()).build().also {
            binding?.playerView?.player = it

            binding?.playerView?.hideController()
            binding?.playerView?.setControllerVisibilityListener {
                if (it == View.VISIBLE) {
                    binding?.playerView?.hideController()
                }
            }
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.playerView?.onPause()
        simpleExoPlayer?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
    }
}