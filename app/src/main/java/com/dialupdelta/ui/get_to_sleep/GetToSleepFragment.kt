package com.dialupdelta.ui.get_to_sleep

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.dialupdelta.R
import com.dialupdelta.`interface`.ProgramClickPosition
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.data.preferences.PreferenceProvider
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.databinding.FragmentGetToSleepBinding
import com.dialupdelta.ui.get_to_sleep.adapter.NewAdapterGetToSleep
import com.dialupdelta.ui.library.LibraryModulesActivity
import com.dialupdelta.ui.login_signup.LoginActivity
import com.dialupdelta.ui.sleep_enhancer.AlarmReceiver
import com.dialupdelta.ui.sleep_enhancer.AlarmService
import com.dialupdelta.ui.sleep_enhancer.LocalSaveSleepEnhancer
import com.dialupdelta.ui.transition.TransitionActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import org.json.JSONObject
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
    private var firstProgram = ""
    private var secondProgram = ""
    private var audio1 = ""
    private var audio2 = ""
    private var dat = ""
    private var programId = 1
    private lateinit var countTimer: CountDownTimer
    private lateinit var binding:FragmentGetToSleepBinding
    private val repository: Repository by instance()


    private val factory: GetToSleepViewModelFactory by instance()
    private lateinit var viewModel: GetToSleepViewModel
    var prefs: PreferenceProvider? = context?.let { PreferenceProvider(it) }

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

        makePostRequest()



//        setAlarm(60*1000)
        videoPlay()
        binding.leftArrowSleep.setOnClickListener {
             Intent(requireActivity(), AnimationOnLeftActivity::class.java).also {
                 startActivity(it)
             }
        }

        binding.logoutTts.setOnClickListener {
            prefs?.saveAuthData(null)
            Intent(requireActivity(), LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.explore.setOnClickListener {
            startActivity(Intent(context, LibraryModulesActivity::class.java))
        }

        binding.skipSleep.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dialogShowOnStartTimer()
            }
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

        try{
            val  dataForSleepEnhancer = viewModel.savedSleepEnhancer()!!

//            Toast.makeText(context, dataForSleepEnhancer.result, Toast.LENGTH_SHORT).show()

        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun setObserver(viewModel: GetToSleepViewModel) {

        viewModel.getToSleepResponse.observe(viewLifecycleOwner) {
           adapterNewGet()
        }

        viewModel.getToSleepVideoResponse.observe(viewLifecycleOwner) {
          getToSleepProgramDialogShow()
        }

        viewModel.successWakeUp.observe(viewLifecycleOwner) {
              dat = it.time
        }

        viewModel.successSleepEnhancer.observe(viewLifecycleOwner) {
            firstAlarm = it.t_1
            secondAlarm = it.t_2
            firstProgram = it.program_1
            secondProgram = it.program_2
            audio1 = it.audio_1
            audio2 = it.audio_2

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

            // find view by id
            val repeatDialog: Button = dialog.findViewById(R.id.repeat_dialog_tts)
            val sleepDialogTts: Button = dialog.findViewById(R.id.sleep_dialog_tts)
            val playerView1 = dialog.findViewById(R.id.playerViewtts) as PlayerView
            val closeID = dialog.findViewById(R.id.closeID) as ImageView
            val exoFullScreenIcon = dialog.findViewById(R.id.exo_fullscreen_icon_tts) as ImageView
            // sleep enhancer
            val imageView1: ImageView = dialog.findViewById(R.id.imageView_1)
            val imageView2: ImageView = dialog.findViewById(R.id.imageView_2)
            val tv1: TextView = dialog.findViewById(R.id.tv_1)
            val tv2: TextView = dialog.findViewById(R.id.tv_2)
            //timer
            val timer: TextView = dialog.findViewById(R.id.timer)
            timer.visibility = View.GONE
            // wakeup
            val ivWakeup: ImageView = dialog.findViewById(R.id.iv_wakeup_)
            val tcWakeup: TextView = dialog.findViewById(R.id.tc_wakeup_)
            val iv2Wakeup: ImageView = dialog.findViewById(R.id.iv2_wakeup_)

            // set data for sleep enhancer
            tv1.text = firstAlarm
            tv2.text = secondAlarm
            Glide.with(requireActivity()).load(firstProgram).into(imageView1)
            Glide.with(requireActivity()).load(secondProgram).into(imageView2)

            // data set for wakeup
            tcWakeup.text = dat

            // set the traits for evening program
            setImageToDashboard(ivWakeup, wakeupTrait)

            val finishTime = player?.duration
            if (Player.STATE_READY == 3) {
                counter(timer, finishTime)
            }

            // repeat the video play in dialog
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

            // video play in full screen
            exoFullScreenIcon.setOnClickListener {
                if (checkFull == "yes") {
                    player?.pause()
                    closeID.visibility = View.INVISIBLE
                    playerView1.visibility = View.INVISIBLE
                    exoFullScreenIcon.visibility = View.INVISIBLE
                }
            }

            // close the dialog
            closeID.setOnClickListener {
                playerView1.onPause()
                playerView1.setKeepContentOnPlayerReset(true)
                player?.pause()
                dialog.dismiss()
            }

            // navigation to sleep enhancer
            sleepDialogTts.setOnClickListener {
                playerView1.onPause()
                playerView1.setKeepContentOnPlayerReset(true)
                player?.pause()
                dialog.dismiss()

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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun dialogShowOnStartTimer() {

        setAlarm((firstAlarm.toLong()) *  60*1000, audio1, 1)

        val dialog = context?.let { Dialog(it, android.R.style.Theme_Black_NoTitleBar_Fullscreen) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialogonstarttimer)
            dialog.show()

            // to black the background
//            val constraintLayout18 : ConstraintLayout = dialog.findViewById(R.id.constraintLayout18)
//            val constraintLayout19 : ConstraintLayout = dialog.findViewById(R.id.constraintLayout19)
//            val constraintLayout20 : ConstraintLayout = dialog.findViewById(R.id.constraintLayout20)
            val cl_cl : ConstraintLayout = dialog.findViewById(R.id.cl_cl)
            val sleep_dialog_tts1: Button = dialog.findViewById(R.id.sleep_dialog_tts_s)
            // sleep enhancer
            val imageView_1: ImageView = dialog.findViewById(R.id.imageView_1s)
            val imageView_2: ImageView = dialog.findViewById(R.id.imageView_2s)
            val tv_1: TextView = dialog.findViewById(R.id.tv_1s)
            val tv_2: TextView = dialog.findViewById(R.id.tv_2s)
            // wakeup
            val iv_wakeup_: ImageView = dialog.findViewById(R.id.iv_wakeup_s)
            val tc_wakeup_: TextView = dialog.findViewById(R.id.tc_wakeup_s)
            val iv2_wakeup_: ImageView = dialog.findViewById(R.id.iv2_wakeup_s)
            // close button
            val sleep_dialog_ttsclose : Button = dialog.findViewById(R.id.sleep_dialog_ttsclose)

            // close dialog
            sleep_dialog_ttsclose.setOnClickListener {
                dialog.dismiss()
            }

            // set sleep enhancer data
            tv_1.text = firstAlarm
            tv_2.text = secondAlarm
            Glide.with(requireActivity()).load(firstProgram).into(imageView_1)
            Glide.with(requireActivity()).load(secondProgram).into(imageView_2)

            // wakeup data
            setImageToDashboard(iv_wakeup_, wakeupTrait)
            tc_wakeup_.text = dat

            // on alarm ring
//            Handler(Looper.getMainLooper()).postDelayed({
//                constraintLayout18.visibility = View.GONE
//                constraintLayout19.visibility = View.GONE
//                constraintLayout20.visibility = View.GONE
//                cl_cl.setBackgroundColor(resources.getColor(R.color.black))
//            }, 30000)

            sleep_dialog_tts1.setOnClickListener {
                dialog.dismiss()
            }
        }
        viewModel.getToSleepSave(genderSelected, durationSelected, programId, 1)
        setAlarm((secondAlarm.toLong()) *  60*1000, audio2, 2)
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
        Log.e("er", ""+programId)
        viewModel.getToSleepVideoList(genderSelected, 5, programId)
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
        durationSelected = 5
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(delay: Long, audioUrl : String, alarmNumber : Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("URL_EXTRA", audioUrl) // Pass the URL as an extra parameter
            putExtra("alarmNumber", ""+alarmNumber) // Pass the URL as an extra parameter
        } // Create a new BroadcastReceiver class for handling alarms
        val requestCode = generateUniqueRequestCode()
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        val currentTime = System.currentTimeMillis()
        val triggerTime = currentTime + delay

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

        // Start the AlarmService
//        val serviceIntent = Intent(context, AlarmService::class.java).apply {
//            putExtra("URL_EXTRA", audioUrl) // Pass the same URL to the service
//            putExtra("alarmNumber", alarmNumber)
//            putExtra("time", delay)
//        }
//        context?.startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        videoPlay()
    }

    private fun generateUniqueRequestCode(): Int {
        // You can use any logic to generate a unique request code, for example, using a timestamp or a random number
        return System.currentTimeMillis().toInt()
    }

    fun makePostRequest() {
        val url = "http://app.dialupdelta.com/web/api/change_language"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        // Form data
        val uid = repository.getAuthData()?.id.toString()
        val languageId = repository.getLanguage().toString()

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                // Handle the response here
                Log.e("Response: ", response+" - "+ uid + " - "+ languageId)

                viewModel = ViewModelProvider(this, factory)[GetToSleepViewModel::class.java]
                setObserver(viewModel)
                viewModel.getToSleepList()
                viewModel.saveGetToSleepApi()
                viewModel.savedSleepEnhancer()
                viewModel.fetchWakeUpSaved()
            },
            Response.ErrorListener { error ->
                // Handle error here
                println("Error: $error")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = uid
                params["language_id"] = languageId
                return params
            }
        }

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
    }

}