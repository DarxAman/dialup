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
import com.dialupdelta.data.network.response.intro_video_response.IntroVideo
import com.dialupdelta.databinding.FragmentSecondVideoBinding
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setVisible
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class SecondVideoFragment : BaseFragment() {
    private var binding:FragmentSecondVideoBinding? = null
    lateinit var navController: NavController
    var simpleExoPlayer: SimpleExoPlayer? = null
    private var newUrl : String = ""
    private lateinit var videoList: IntroVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_second_video, container, false)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        hideStatusBar(requireActivity())
        initUI()
    }

    private fun initUI() {
        videoList = arguments?.getSerializable(MyKeys.introVideoData) as IntroVideo
        newUrl = "${videoList.base_url}${videoList.videos[1].video_file_name}"
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
            val bundle = Bundle()
            bundle.putSerializable(MyKeys.introVideoData, videoList)
            navController.navigate(R.id.action_secondVideoFragment_to_thirdVideoFragment, bundle)
        }

    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
    }

    private fun playVideoClick() {
        val mediaItem: MediaItem = newUrl.let { MediaItem.fromUri(it) }
        simpleExoPlayer = SimpleExoPlayer.Builder(requireActivity()).build().also { it ->
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
            simpleExoPlayer?.volume  = 0f

        }
    }
}