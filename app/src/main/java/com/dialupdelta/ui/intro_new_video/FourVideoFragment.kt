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
import com.dialupdelta.databinding.FragmentFourVideoBinding
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setVisible
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class FourVideoFragment : BaseFragment() {
    private var binding:FragmentFourVideoBinding? = null
    var player: SimpleExoPlayer? = null
    lateinit var navController: NavController
    var newUrl : String = "https://app.whyuru.com/assets/screen_video/screen_4.mp4"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_four_video, container, false)
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

        player?.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    binding?.nextBtn?.setVisible()
                }
            }
        })

        binding?.nextBtn?.setOnClickListener {
           binding?.playerView?.onPause()
            player?.pause()
            player?.stop()
            navController.navigate(R.id.action_fourVideoFragment_to_fiveVideoFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }

    private fun playVideoClick() {
        val mediaItem: MediaItem = newUrl.let { MediaItem.fromUri(it) }
        player = SimpleExoPlayer.Builder(requireActivity()).build().also {
            binding?.playerView?.player = it
            binding?.playerView?.hideController()
            binding?.playerView?.setControllerVisibilityListener {
                if(it == View.VISIBLE){
                    binding?.playerView?.hideController()
                }
            }
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()

            player?.volume  = 0f
        }
    }

}