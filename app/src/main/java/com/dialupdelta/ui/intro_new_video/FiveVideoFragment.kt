package com.dialupdelta.ui.intro_new_video

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentFiveVideoBinding
import com.dialupdelta.ui.get_start_activity.GetStartActivity
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setVisible
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class FiveVideoFragment : BaseFragment() {
 private var binding:FragmentFiveVideoBinding? = null
    var player:SimpleExoPlayer? = null
    var newUrl:String = "https://app.whyuru.com/assets/screen_video/screen_5.mp4"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_five_video, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            Intent(context, GetStartActivity::class.java).also {
                startActivity(it)
            }
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