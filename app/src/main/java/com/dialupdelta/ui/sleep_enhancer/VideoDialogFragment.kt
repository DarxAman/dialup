package com.dialupdelta.ui.sleep_enhancer

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.DialogFragment
import com.dialupdelta.R

class VideoDialogFragment : DialogFragment() {
    private lateinit var videoView: VideoView

    companion object {
        private const val ARG_VIDEO_URL = "videoUrl"
        private const val ARG_ALARM_NUMBER = "alarmNumber"

        fun newInstance(videoUrl: String, alarmNumber: String): VideoDialogFragment {
            val fragment = VideoDialogFragment()
            val args = Bundle()
            args.putString(ARG_VIDEO_URL, videoUrl)
            args.putString(ARG_ALARM_NUMBER, alarmNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_dialog_video, container, false)
        videoView = view.findViewById(R.id.videoView)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrl = arguments?.getString(ARG_VIDEO_URL)
        if (!videoUrl.isNullOrEmpty()) {
            val videoUri = Uri.parse(videoUrl)

            videoView.setVideoURI(videoUri)
            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
            }

            videoView.setOnCompletionListener {
                dismiss() // Dismiss the dialog when video playback is complete
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoView.stopPlayback()
    }
}
