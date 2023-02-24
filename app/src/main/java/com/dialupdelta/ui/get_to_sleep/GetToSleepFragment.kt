package com.dialupdelta.ui.get_to_sleep

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.dialupdelta.R
import com.dialupdelta.`interface`.ProgramClickPosition
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentGetToSleepBinding
import com.dialupdelta.ui.get_to_sleep.adapter.NewAdapterGetToSleep
import com.dialupdelta.ui.library.LibraryModulesActivity
import com.dialupdelta.ui.login_signup.LoginActivity
import com.dialupdelta.ui.transition.TransitionActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import java.util.ArrayList

class GetToSleepFragment : BaseFragment(), ProgramClickPosition {
    private var idC: MutableList<String> = ArrayList()
    private var fileName: MutableList<String> = ArrayList()
    private var genderL: MutableList<String> = ArrayList()
    private var languages: MutableList<String> = ArrayList()
    private var durationL: MutableList<String> = ArrayList()
    private var thumb: MutableList<String> = ArrayList()
    private var fileURL: MutableList<String> = ArrayList()
    private  var wakeupTrait:String = ""
    private var position = 0
    private var isGenderCLick = "male"
    private var userId: Int = 0
    private var genderSelected = "male"
    private var durationSelected = "5min"
    private var programSelected = ""
    private var checkFull = ""
    private var player: SimpleExoPlayer? = null
    private var newUrl: String = ""
    private var firstAlarm = ""
    private var secondAlarm = ""
    private var thirdAlarm = ""
    private var forthAlarm = ""
    private var trait1 = ""
    private var trait2 = ""
    private var dat = ""
    private var clickedImageURL = ""
    private lateinit var countTimer: CountDownTimer
    private lateinit var playerView1: PlayerView
    private var idforProgram: MutableList<String> = ArrayList()
    private var programName: MutableList<String> = ArrayList()
    private var programPic: MutableList<String> = ArrayList()
    private lateinit var binding:FragmentGetToSleepBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_get_to_sleep, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        // add static id in list
        idforProgram.add("1")
        idforProgram.add("2")
        idforProgram.add("3")
        idforProgram.add("4")

        // add static program name in list
        programName.add("Program A")
        programName.add("Program B")
        programName.add("Program C")
        programName.add("Program D")

        // add static images in list
        programPic.add("https://projectblinkit.s3.ap-south-1.amazonaws.com/imageA.png")
        programPic.add("https://projectblinkit.s3.ap-south-1.amazonaws.com/imageB.png")
        programPic.add("https://projectblinkit.s3.ap-south-1.amazonaws.com/imageC.png")
        programPic.add("https://projectblinkit.s3.ap-south-1.amazonaws.com/imageD.png")

        videoPlay()

        binding.leftArrowSleep.setOnClickListener {
             Intent(requireActivity(), AnimationOnLeftActivity::class.java).also {
                 startActivity(it)
             }
        }

        binding.logoutTts.setOnClickListener {
            Intent(requireActivity(), LoginActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.explore.setOnClickListener {
            startActivity(Intent(context, LibraryModulesActivity::class.java))
        }

        binding.tv45min.setOnClickListener {
            binding.tv90min.setTextColor(Color.GRAY)
            binding.tv45min.setTextColor(Color.WHITE)

            if (isGenderCLick == "male") {
                durationSelected = "5min"
                genderSelected = "male"
                adapterNewGet()
            } else {
                durationSelected = "5min"
                genderSelected = "female"
                adapterNewGet()
            }
        }
    }

    private fun dialogShow() {
        val dialog = context?.let { Dialog(it, android.R.style.Theme_Black_NoTitleBar_Fullscreen) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_splash)
            dialog.show()

            val repeatDialog: Button = dialog.findViewById(R.id.repeat_dialog_tts)
            val sleepDialogTts: Button = dialog.findViewById(R.id.sleep_dialog_tts)

            val playerView1 = dialog.findViewById(R.id.playerViewtts) as PlayerView
            val closeID = dialog.findViewById(R.id.closeID) as ImageView
            val exoFullScreenIcon = dialog.findViewById(R.id.exo_fullscreen_icon_tts) as ImageView

            val imageView1: ImageView = dialog.findViewById(R.id.imageView_1)
            val imageView2: ImageView = dialog.findViewById(R.id.imageView_2)
            val imageView3: ImageView = dialog.findViewById(R.id.imageView_3)
            val imageView4: ImageView = dialog.findViewById(R.id.imageView_4)

//            imageView_1.setIM =
            setImageToDashboard(imageView3, trait1)
            setImageToDashboard(imageView4, trait2)


            val timer: TextView = dialog.findViewById(R.id.timer)
            val tv1: TextView = dialog.findViewById(R.id.tv_1)
            val tv2: TextView = dialog.findViewById(R.id.tv_2)
            val tv3: TextView = dialog.findViewById(R.id.tv_3)
            val tv4: TextView = dialog.findViewById(R.id.tv_4)

            tv1.text = firstAlarm
            tv2.text = secondAlarm
            tv3.text = thirdAlarm
            tv4.text = forthAlarm

            val ivWakeup: ImageView = dialog.findViewById(R.id.iv_wakeup_)
            val tcWakeup: TextView = dialog.findViewById(R.id.tc_wakeup_)
            val iv2Wakeup: ImageView = dialog.findViewById(R.id.iv2_wakeup_)

            setImageToDashboard(ivWakeup, wakeupTrait)
            //  user glide method setUserImage
            //  Picasso.get().load(clickedImageURL).fit().centerCrop().noFade().into(iv2Wakeup)

            tcWakeup.text = dat
            val finishTime = player?.duration
            if (Player.STATE_READY == 3) {
                counter(timer, finishTime)
            }
            repeatDialog.setOnClickListener {
                playerView1.onPause()
                playerView1.setKeepContentOnPlayerReset(true)
                player?.pause()

                newUrl = fileURL.get(0)
                click()

                if (countTimer == null) {
                    counter(timer, finishTime)
                } else {
                    countTimer.cancel()
                    counter(timer, finishTime)
                }
            }

            click()

            exoFullScreenIcon.setOnClickListener {
                if (checkFull == "yes") {
                    player?.pause()
                    closeID.visibility = View.INVISIBLE
                    playerView1.visibility = View.INVISIBLE
                    exoFullScreenIcon.visibility = View.INVISIBLE
//                    val intent = Intent(context, VideoActivity::class.java)
//                    intent.putExtra(SyncStateContract.Constants.VIDEO_LINK, newUrl)
                    //  requireContext().startActivity(intent)
                }
            }


            closeID.setOnClickListener {
                playerView1.onPause()
                playerView1.setKeepContentOnPlayerReset(true)
                player?.pause()
                dialog.dismiss()
            }

            sleepDialogTts.setOnClickListener {
                if (userId == 0) {
                    (activity as TransitionActivity).navigateToSleepEnhancerFragment()
                    dialog.dismiss()
                } else {
                    (activity as TransitionActivity).navigateToSleepEnhancerFragment()
                    dialog.dismiss()
                }
            }
        }
    }

    private fun dialogShowOnStartTimer() {
        val dialog = context?.let { Dialog(it, android.R.style.Theme_Black_NoTitleBar_Fullscreen) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialogonstarttimer)
            dialog.show()


            val constraintLayout18 : ConstraintLayout = dialog.findViewById(R.id.constraintLayout18)
            val constraintLayout19 : ConstraintLayout = dialog.findViewById(R.id.constraintLayout19)
            val constraintLayout20 : ConstraintLayout = dialog.findViewById(R.id.constraintLayout20)
            val cl_cl : ConstraintLayout = dialog.findViewById(R.id.cl_cl)

            val sleep_dialog_tts1: Button = dialog.findViewById(R.id.sleep_dialog_tts_s)
            val imageView_1: ImageView = dialog.findViewById(R.id.imageView_1s)
            val imageView_2: ImageView = dialog.findViewById(R.id.imageView_2s)
            val imageView_3: ImageView = dialog.findViewById(R.id.imageView_3s)
            val imageView_4: ImageView = dialog.findViewById(R.id.imageView_4s)

            val sleep_dialog_ttsclose : Button = dialog.findViewById(R.id.sleep_dialog_ttsclose)
            sleep_dialog_ttsclose.setOnClickListener {
                dialog.dismiss()
            }

            setImageToDashboard(imageView_3, trait1)
            setImageToDashboard(imageView_4, trait2)

            val tv_1: TextView = dialog.findViewById(R.id.tv_1s)
            val tv_2: TextView = dialog.findViewById(R.id.tv_2s)
            val tv_3: TextView = dialog.findViewById(R.id.tv_3s)
            val tv_4: TextView = dialog.findViewById(R.id.tv_4s)

            tv_1.text = firstAlarm
            tv_2.text = secondAlarm
            tv_3.text = thirdAlarm
            tv_4.text = forthAlarm


            val iv_wakeup_: ImageView = dialog.findViewById(R.id.iv_wakeup_s)
            val tc_wakeup_: TextView = dialog.findViewById(R.id.tc_wakeup_s)
            val iv2_wakeup_: ImageView = dialog.findViewById(R.id.iv2_wakeup_s)

            setImageToDashboard(iv_wakeup_, wakeupTrait)

            //Picasso.get().load(clickedImageURL).fit().centerCrop().noFade().into(iv2_wakeup_)

            tc_wakeup_.text = dat

            Handler(Looper.getMainLooper()).postDelayed({
                constraintLayout18.visibility = View.GONE
                constraintLayout19.visibility = View.GONE
                constraintLayout20.visibility = View.GONE
                cl_cl.setBackgroundColor(resources.getColor(R.color.black))
            }, 30000)

            sleep_dialog_tts1.setOnClickListener {

                val sharedPreferences: SharedPreferences =
                    requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
                val time1 = sharedPreferences.getString("se_time1", "")
                val time2 = sharedPreferences.getString("se_time2", "")
                val url1 = sharedPreferences.getString("se_url", "")

                val time3 = sharedPreferences.getString("ep_time1", "")
                val time4 = sharedPreferences.getString("ep_time2", "")
                val url2 = sharedPreferences.getString("ep1_url", "")
                val url3 = sharedPreferences.getString("ep2_url", "")

                if(Integer.parseInt(time1) != 0){
//                    go(Integer.parseInt(time1), url1, "sleep1")
                }
                if(Integer.parseInt(time2) != 0){
//                    go(Integer.parseInt(time2), url1, "sleep1")
                }

                //            if (url2 != null) {
                if(Integer.parseInt(time3) != 0  /*&& url2.equals("0")*/){
//                    go(Integer.parseInt(time3 ), url2, "sleep_enhancer_2")
                }
//            }

//            if (url3 != null) {
                if(Integer.parseInt(time4) != 0  /*&& url3.equals("0")*/){
//                    go(Integer.parseInt(time4 ), url3, "sleep_enhancer_2")
                }

//                go(Integer.parseInt(time3), url2)
//                go(Integer.parseInt(time4), url3)

                dialog.dismiss()

            }
        }
    }

    private fun setImageToDashboard(imageView_3: ImageView, trait1: String) {
        when (trait1) {
            "O" -> {
                imageView_3.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.setting_o))
            }
            "C" -> {
                imageView_3.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.setting_c))
            }
            "E" -> {
                imageView_3.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.setting_e))
            }
            "A" -> {
                imageView_3.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.setting_a))
            }
            "N" -> {
                imageView_3.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.setting_n))
            }
            else -> {
                imageView_3.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.logo_toolbar))
            }
        }
    }

    private fun counter(timer: TextView, finishTime: Long?) {
        var timerStart = 0L
        if (durationSelected == "5min") {
            timerStart = 310000
        } else {
            timerStart = 550000
        }
        countTimer = object : CountDownTimer(timerStart, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {

                var x = millisUntilFinished / 1000
                var y = x / 60
                if (x % 60 == 0L) {
                    if (y == -1L) {
                        y = 0L
                    } else {
                        y -= 1L
                    }
                }
                timer.text = "" + y + ":" + x % 60

            }
            override fun onFinish() {
                timer.text = "Timer Started"
            }
        }.start()
    }

    private fun adapterNewGet() {
        val transitionToSleepAdapter = NewAdapterGetToSleep(
            context,
            programName,
            idforProgram,
            genderSelected,
            durationSelected,
            this,
            programPic
        )
        binding.transitionToSleepRecycler.setHasFixedSize(true)
        binding.transitionToSleepRecycler.layoutManager = GridLayoutManager(context, 2)
        binding.transitionToSleepRecycler.adapter = transitionToSleepAdapter
    }

    private fun videoPlay() {
        val mediaController = MediaController(context)
        mediaController.setMediaPlayer(binding.videoView)
        val uri = Uri.parse("android.resource://" + context?.packageName + "/R.raw/" + R.raw.transitionvidnew)
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()
        binding.videoView.setOnPreparedListener { mp -> mp.isLooping = true }
    }

    private fun click() {
        val mediaItem: MediaItem = newUrl.let { MediaItem.fromUri(it) }
        player = SimpleExoPlayer.Builder(requireContext()).build().also {
            playerView1.player = it
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
            player?.volume = 10f
        }
    }

    override fun clickIdForProgram(id: String?, position: Int) {

    }
}