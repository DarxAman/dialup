package com.dialupdelta.ui.sleep_enhancer

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentSleepEnhancerBinding
import java.util.ArrayList

class SleepEnhancerFragment : BaseFragment() {
    private lateinit var binding:FragmentSleepEnhancerBinding
    private var left1count = 0
    private var right1count = 0
    private var left2count = 0
    private var right2count = 0
    private var answerForLeft = 45
    private var answerForRight = 135
    private var toXdelta1 = 0.0f
    private var toXdelta2 = 0.0f
    private var negXdelta1 = 0.0f
    private var negXdelta2 = 0.0f
    private var id45: MutableList<String> = ArrayList()
    private var fileName: MutableList<String> = ArrayList()
    private var fileURL: MutableList<String> = ArrayList()
    private var isActive: MutableList<String> = ArrayList()
    private var currentVolume = 0
    private lateinit var audioManager: AudioManager
    private var seekbarPosition = 0
    var showSetAlarmLeft = 0
    var showSetAlarmRight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_enhancer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        hideOptions()
        hideOptionRight()

        binding.seekBar111.max = 100
        binding.seekBar111.progress = currentVolume

        binding.resetBtn.setOnClickListener {
            hideOptions()
            hideOptionRight()
            answerForLeft = 0
            answerForRight = 0
            binding.showOptionCLickRight.visibility = View.VISIBLE
            binding.showOptionclick.visibility = View.VISIBLE
        }

        binding.showOptionclick.setOnClickListener {
            showOptions()
            binding.showOptionclick.visibility = View.INVISIBLE
            showSetAlarmLeft = 1
        }
        binding.showOptionCLickRight.setOnClickListener {
            showOptionRight()
            binding.showOptionCLickRight.visibility = View.INVISIBLE
            showSetAlarmRight = 1
        }

        transitionClickListener()
        try {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

            binding.seekBar111.max = 100
            binding.seekBar111.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
//                    Toast.makeText(context, "0000000" + 1, Toast.LENGTH_SHORT).show()
                    binding.seekBar111.progress = progress

                    // after development
//                    val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
//                    val editor: SharedPreferences.Editor =  sharedPreferences.edit()
////                    Global.ids1 = jsonObject1.getString("id")
//                    editor.putString("Volume_Sleep_Enhancer", ""+ progress)
//                    editor.apply()
//                    editor.commit()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun transitionClickListener() {
        // right arrow for first image slide
        binding.arrowRight1.setOnClickListener {
            showSetAlarmLeft = 1
            left1count = 0
            if (right1count <= 10) {
                right1count+=5
                answerForLeft += 5
                binding.showAdd1.text = "(+$right1count)"
                if (right1count > 0)
                    binding.showAdd1.setTextColor(Color.GREEN)
                else
                    binding.showAdd1.setTextColor(Color.RED)
                toXdelta1 += 20.0f
                val animation = TranslateAnimation(0f, toXdelta1, 0f, 0f)
                animation.duration = 1000
                animation.fillAfter = true
                binding.bottom1.startAnimation(animation)
            } else {
                Toast.makeText(context, "not allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // left arrow for first image slide
        binding.arrowLeft1.setOnClickListener {
            right1count = 0
            showSetAlarmLeft = 1
            if (left1count >= -10) {
                left1count-=5
                answerForLeft -= 5
                if (left1count < 0) {
                    binding.showAdd1.setTextColor(Color.RED)
                    binding.showAdd1.text = "($left1count)"
                } else {
                    binding.showAdd1.setTextColor(Color.GREEN)
                    binding.showAdd1.text = "($left1count)"
                }
                negXdelta1 -= 20
                val animation = TranslateAnimation(0f, negXdelta1, 0f, 0f)
                animation.duration = 1000
                animation.fillAfter = true
                binding.bottom1.startAnimation(animation)
            } else {
                Toast.makeText(context, "not allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // left arrow for second image slide
        binding.arrowLeft2.setOnClickListener {
            right2count = 0
            showSetAlarmRight = 1
            if (left2count >= -10) {
                left2count-=5
                answerForRight -= 5
                binding.showAdd2.text = "($left2count)"
                if (left2count < 0)
                    binding.showAdd2.setTextColor(Color.RED)
                else
                    binding.showAdd2.setTextColor(Color.GREEN)
                negXdelta2 -= 20
                val animation = TranslateAnimation(0f, negXdelta2, 0f, 0f)
                animation.duration = 1000
                animation.fillAfter = true
                binding.bottom2.startAnimation(animation)
            } else {
                Toast.makeText(context, "not allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // right arrow for second image slide
        binding.arrowRight2.setOnClickListener {
            left2count = 0

            showSetAlarmRight = 1
            if (right2count <= 10) {
                right2count+=5
                answerForRight += 5
                binding.showAdd2.text = "(+$right2count)"
                if (right2count > 0)
                    binding.showAdd2.setTextColor(Color.GREEN)
                else
                    binding.showAdd2.setTextColor(Color.RED)
                toXdelta2 += 20.0f
                val animation = TranslateAnimation(0f, toXdelta2, 0f, 0f)
                animation.duration = 1000
                animation.fillAfter = true
                binding.bottom2.startAnimation(animation)
            } else {
                Toast.makeText(context, "not allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // center first block
        binding.showAns.setOnClickListener {
            answerForLeft = 45
            right1count = 0
            left1count = 0
            negXdelta1 = 0.0f
            toXdelta1 = 0.0f
            binding.showAdd1.text = "  "
            val animation = TranslateAnimation(0f, 0f, 0f, 0f)
            animation.duration = 1000
            animation.fillAfter = true
            binding.bottom1.startAnimation(animation)
        }

        // center icon for first image slide to position 0 ( 45 min )
        binding.center1.setOnClickListener {
            answerForLeft = 45
            right1count = 0
            left1count = 0
            negXdelta1 = 0.0f
            toXdelta1 = 0.0f
            binding.showAdd1.text = "  "
            val animation = TranslateAnimation(0f, 0f, 0f, 0f)
            animation.duration = 1000
            animation.fillAfter = true
            binding.bottom1.startAnimation(animation)
        }


        // center second block
        binding.showAns2.setOnClickListener {
            answerForRight = 135
            right2count = 0
            left2count = 0
            negXdelta2 = 0.0f
            toXdelta2 = 0.0f
            binding.showAdd2.text = "  "
            val animation = TranslateAnimation(0f, 0f, 0f, 0f)
            animation.duration = 1000
            animation.fillAfter = true
            binding.bottom2.startAnimation(animation)
        }

        // center icon for second image slide to position 0 ( 135 min )
        binding.center2.setOnClickListener {
            answerForRight = 135
            right2count = 0
            left2count = 0
            negXdelta2 = 0.0f
            toXdelta2 = 0.0f
            binding.showAdd2.text = "  "
            val animation = TranslateAnimation(0f, 0f, 0f, 0f)
            animation.duration = 1000
            animation.fillAfter = true
            binding.bottom2.startAnimation(animation)
        }
        videoPlay()
    }

    private fun videoPlay() {
        val mediaController = MediaController(context)
        val uri = Uri.parse("android.resource://" + context?.packageName + "/R.raw/" + R.raw.eveningvideo)
        binding.sleepVideo.setVideoURI(uri)
        binding.sleepVideo.start()
        binding.sleepVideo.setOnPreparedListener { mp -> mp.isLooping = true }
    }

    private fun hideOptions() {
        binding.showAns.visibility = View.INVISIBLE
        binding.bottom1.visibility = View.INVISIBLE
        binding.arrowLeft1.visibility = View.INVISIBLE
        binding.arrowRight1.visibility = View.INVISIBLE
        binding.showAdd1.visibility = View.INVISIBLE
    }

    private fun hideOptionRight() {
        binding.showAns2.visibility = View.INVISIBLE
        binding.bottom2.visibility = View.INVISIBLE

        binding.arrowLeft2.visibility = View.INVISIBLE
        binding.arrowRight2.visibility = View.INVISIBLE
        binding.showAdd2.visibility = View.INVISIBLE
    }

    private fun showOptions() {
        binding.showAns.visibility = View.VISIBLE
        binding.bottom1.visibility = View.VISIBLE
        binding.arrowRight1.visibility = View.VISIBLE
        binding.arrowLeft1.visibility = View.VISIBLE
        binding.showAdd1.visibility = View.VISIBLE
    }

    private fun showOptionRight() {
        binding.showAns2.visibility = View.VISIBLE
        binding.bottom2.visibility = View.VISIBLE
        binding.arrowLeft2.visibility = View.VISIBLE
        binding.arrowRight2.visibility = View.VISIBLE
        binding.showAdd2.visibility = View.VISIBLE
    }
}