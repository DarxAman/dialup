package com.dialupdelta.ui.get_to_sleep

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import org.kodein.di.generic.instance

class GetToSleepFragment : BaseFragment(), ProgramClickPosition {
    private  var wakeupTrait:String = ""
    private var userId: Int = 0
    private var genderSelected = 1
    private var durationSelected = 5
    private var checkFull = ""
    private var player: SimpleExoPlayer? = null
    private var firstAlarm = ""
    private var secondAlarm = ""
    private var thirdAlarm = ""
    private var forthAlarm = ""
    private var trait1 = ""
    private var trait2 = ""
    private var dat = ""
    private var programId = 1
    private lateinit var countTimer: CountDownTimer
    private lateinit var binding:FragmentGetToSleepBinding

    private val factory: GetToSleepViewModelFactory by instance()
    private lateinit var viewModel: GetToSleepViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_get_to_sleep, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[GetToSleepViewModel::class.java]
        setObserver(viewModel)
        viewModel.getToSleepList()

        viewModel.saveGetToSleepApi()

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

        binding.skipSleep.setOnClickListener {
            dialogShowOnStartTimer()
        }

        binding.skipToProgram.setOnClickListener {
            (activity as TransitionActivity).navigateToWakeUpFragment()
        }

        binding.tv45min.setOnClickListener {
            selected5Mint()
        }

        binding.tv90min.setOnClickListener {
            selected9Mint()
        }

        binding.sleepFemale.setOnClickListener {
            selectedFemale()
        }

        binding.sleepMale.setOnClickListener {
            selectedMale()
        }
    }

    private fun setObserver(viewModel: GetToSleepViewModel) {

        viewModel.getToSleepResponse.observe(viewLifecycleOwner) {
           adapterNewGet()
        }

        viewModel.getToSleepVideoResponse.observe(viewLifecycleOwner) {
          getToSleepProgramDialogShow()
        }

        viewModel.getSaveSleepResponse.observe(viewLifecycleOwner){
            if (it.duration_id == 9){
                selected9Mint()
            }
            else{
                selected5Mint()
            }
            if (it.gender_id == 1){
                selectedMale()
            }
            else{
                selectedFemale()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }

    private fun getToSleepProgramDialogShow() {
        val dialog = context?.let { Dialog(it, android.R.style.Theme_Black_NoTitleBar_Fullscreen) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_splash)

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
                dialogVideoClickPlay(playerView1)

                if (countTimer == null) {
                    counter(timer, finishTime)
                } else {
                    countTimer.cancel()
                    counter(timer, finishTime)
                }
            }

            dialogVideoClickPlay(playerView1)

            exoFullScreenIcon.setOnClickListener {
                if (checkFull == "yes") {
                    player?.pause()
                    closeID.visibility = View.INVISIBLE
                    playerView1.visibility = View.INVISIBLE
                    exoFullScreenIcon.visibility = View.INVISIBLE
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
        dialog?.show()
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
            tc_wakeup_.text = dat
            Handler(Looper.getMainLooper()).postDelayed({
                constraintLayout18.visibility = View.GONE
                constraintLayout19.visibility = View.GONE
                constraintLayout20.visibility = View.GONE
                cl_cl.setBackgroundColor(resources.getColor(R.color.black))
            }, 30000)

            sleep_dialog_tts1.setOnClickListener {
                dialog.dismiss()
            }
        }
        viewModel.getToSleepSave(genderSelected, durationSelected, programId, 1)
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
        timerStart = if (durationSelected == 5) {
            310000
        } else {
            550000
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
        val transitionToSleepAdapter = NewAdapterGetToSleep(requireActivity(), this, viewModel.getToSleepProgramList())
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

    private fun dialogVideoClickPlay(playerView: PlayerView) {
        val videoBaseUrl = viewModel.getToSleepVideoData()?.base_url_video
        val videoSubUrl = viewModel.getToSleepVideoData()?.list?.get(0)?.video_url
        val completeVideoUrl = "$videoBaseUrl/$videoSubUrl"
        val mediaItem: MediaItem = completeVideoUrl.let { MediaItem.fromUri(it) }
        player = SimpleExoPlayer.Builder(requireActivity()).build().also {
            playerView.player = it
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
            player?.volume = 10f
        }
    }

    override fun clickIdForProgram(id: String?, position: Int) {

    }

    override fun getToSleepProgramItemListener(position: Int) {
        programId = viewModel.getToSleepProgramList()?.get(position)?.id!!
        viewModel.getToSleepVideoList(genderSelected, 1, programId)
    }


    private fun selectedFemale(){
        binding.sleepMale.setTextColor(Color.GRAY)
        binding.sleepFemale.setTextColor(Color.WHITE)
        genderSelected = 2
    }

    private fun selectedMale(){
        binding.sleepMale.setTextColor(Color.WHITE)
        binding.sleepFemale.setTextColor(Color.GRAY)
        genderSelected = 1
    }

    private fun selected5Mint(){
        binding.tv90min.setTextColor(Color.GRAY)
        binding.tv45min.setTextColor(Color.WHITE)
        durationSelected = 5
    }

    private fun selected9Mint() {
        binding.tv45min.setTextColor(Color.GRAY)
        binding.tv90min.setTextColor(Color.WHITE)
        durationSelected = 9
    }
}