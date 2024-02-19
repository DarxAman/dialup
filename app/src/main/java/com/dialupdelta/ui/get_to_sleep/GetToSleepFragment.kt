package com.dialupdelta.ui.get_to_sleep

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
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
import androidx.cardview.widget.CardView
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
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.data.preferences.PreferenceProvider
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.databinding.FragmentGetToSleepBinding
import com.dialupdelta.`interface`.ProgramClickPosition
import com.dialupdelta.ui.get_to_sleep.adapter.GetClickedMp3
import com.dialupdelta.ui.get_to_sleep.adapter.Mp3Recycler
import com.dialupdelta.ui.get_to_sleep.adapter.NewAdapterGetToSleep
import com.dialupdelta.ui.library.LibraryModulesActivity
import com.dialupdelta.ui.sleep_enhancer.AlarmReceiver
import com.dialupdelta.ui.sleep_enhancer.CommonObject
import com.dialupdelta.ui.sleep_enhancer.adapter.DudSongsResponse
import com.dialupdelta.ui.splash.SplashActivity
import com.dialupdelta.ui.transition.TransitionActivity
import com.dialupdelta.utils.CustomProgressDialog
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.gson.Gson
import org.json.JSONObject
import org.kodein.di.generic.instance
import java.io.Serializable
import java.util.Calendar

class GetToSleepFragment : BaseFragment(), ProgramClickPosition, Serializable, RingedAlarm, GetClickedMp3 {
    private var wakeupTrait: String = ""
    private var userId: Int = 0
    private var genderSelected = 3
    private var durationSelected = 5
    private var checkFull = ""
    private var player: SimpleExoPlayer? = null
    private var firstAlarm = ""
    private var secondAlarm = ""
    private var firstProgram = ""
    private var secondProgram = ""
    private var audio1 = ""
    private var audio2 = ""
    private var dat = "0:00"
    private var dat_image = ""
    private var dat_url = ""
    private var programId = 1
    private lateinit var countTimer: CountDownTimer
    private lateinit var binding: FragmentGetToSleepBinding
    private val repository: Repository by instance()

    private val factory: GetToSleepViewModelFactory by instance()
    private lateinit var viewModel: GetToSleepViewModel
    var prefs: PreferenceProvider? = context?.let { PreferenceProvider(it) }
    lateinit var imageView_1 : ImageView
    lateinit var imageView_2 : ImageView
    lateinit var iv_wakeup_ : ImageView
    lateinit var imageView1 : ImageView
    lateinit var imageView2 : ImageView
    lateinit var ivWakeup : ImageView
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    val selectedCards = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_get_to_sleep, container, false)
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

        binding.backgroundMusicStop.setOnClickListener {
            binding.bgcMCl.visibility = View.GONE
            binding.loaderProgressBar.visibility = View.GONE

            mediaPlayer.stop()
            mediaPlayer.reset()

            mediaPlayer.setOnCompletionListener {
                binding.bgcMCl.visibility = View.GONE
            }
        }

        binding.logoutTts.setOnClickListener {
            repository.saveAuthData(null)
            repository.clearLogin()

            Log.e("getLogin  : ", repository.getLogin().toString())
            Log.e("getLogin  : ", repository.getAuthData().toString())

            Intent(requireActivity(), SplashActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.explore.setOnClickListener {
            startActivity(Intent(context, LibraryModulesActivity::class.java))
        }

        binding.constraintLayout2.setOnClickListener {
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

        getMp3()

        try {
            val dataForSleepEnhancer = viewModel.savedSleepEnhancer()!!

//            Toast.makeText(context, dataForSleepEnhancer.result, Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setObserver(viewModel: GetToSleepViewModel) {

        viewModel.getToSleepResponse.observe(viewLifecycleOwner) {
            adapterNewGet()
        }

        viewModel.getToSleepVideoResponse.observe(viewLifecycleOwner) {
//            getToSleepProgramDialogShow()
                getToSleepAudioProgram()
        }

        viewModel.successWakeUp.observe(viewLifecycleOwner) {
            if (!it.time.isNullOrEmpty()) {
                dat = it.time
            }
            dat_image = it.thumbURL
            dat_url = it.videoURL
        }

       getRefreshedAlarm()

        viewModel.getSaveSleepResponse.observe(viewLifecycleOwner) {
            if (it.duration_id == 9) {
                selected9Mint()
            } else {
                selected5Mint()
            }
            if (it.gender_id == 1) {
                selectedMale()
            } else {
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

    private fun getRefreshedAlarm() {
//        val customDialog = CustomProgressDialog(requireContext())
//        customDialog.showSweetDialog()
        viewModel.successSleepEnhancer.observe(viewLifecycleOwner) {
            if (it.t_1.equals("0")) {
                firstAlarm = "0"
                firstProgram = ""
                audio1 = ""
            } else {
                firstAlarm = it.t_1
                firstProgram = it.program_1
                audio1 = it.audio_1
            }

            if (it.t_2.equals("0")) {
                secondAlarm = "0"
                secondProgram = ""
                audio2 = ""
            } else {
                secondAlarm = it.t_2
                secondProgram = it.program_2
                audio2 = it.audio_2
            }

        }
//        customDialog.dismissSweet()
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
             imageView1 = dialog.findViewById(R.id.imageView_1)
             imageView2 = dialog.findViewById(R.id.imageView_2)
            val tv1: TextView = dialog.findViewById(R.id.tv_1)
            val tv2: TextView = dialog.findViewById(R.id.tv_2)
            //timer
            val timer: TextView = dialog.findViewById(R.id.timer)
            timer.visibility = View.GONE
            // wakeup
             ivWakeup = dialog.findViewById(R.id.iv_wakeup_)
            val tcWakeup: TextView = dialog.findViewById(R.id.tc_wakeup_)

            // set data for sleep enhancer
            tv1.text = firstAlarm
            tv2.text = secondAlarm
            Glide.with(requireActivity()).load(firstProgram).placeholder(R.drawable.alarm_off)
                .into(imageView1)
            Glide.with(requireActivity()).load(secondProgram).placeholder(R.drawable.alarm_off)
                .into(imageView2)

            // data set for wakeup
            tcWakeup.text = dat
            Glide.with(requireActivity()).load(dat_image).placeholder(R.drawable.alarm_off)
                .into(ivWakeup)


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

        if (!firstAlarm.equals("0")) {
            setAlarm((firstAlarm.toLong()) * 60 * 1000, audio1, 1)
        }

        val dialog = context?.let { Dialog(it, android.R.style.Theme_Black_NoTitleBar_Fullscreen) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialogonstarttimer)
            dialog.show()

            val refreshbtn : Button = dialog.findViewById(R.id.refresh_alarm)

            refreshbtn.setOnClickListener{
                val customDialog = CustomProgressDialog(requireContext())
                customDialog.showSweetDialog()

                Handler(Looper.getMainLooper()).postDelayed({
                   customDialog.dismissSweet()
                }, 1500)

                getRefreshedAlarm()

//                customDialog.dismissSweet()
            }

            val cl_cl: ConstraintLayout = dialog.findViewById(R.id.cl_cl)
            val backDialogButton: Button = dialog.findViewById(R.id.backDialogButton)
            val cancelAlarm: Button = dialog.findViewById(R.id.cancelAlarm)
            
            backDialogButton.setOnClickListener {

                cl_cl.visibility = View.VISIBLE
                backDialogButton.visibility = View.GONE
                refreshbtn.visibility = View.VISIBLE
            }
            cl_cl.visibility = View.VISIBLE
            // sleep enhancer
             imageView_1 = dialog.findViewById(R.id.imageView_1s)
             imageView_2 = dialog.findViewById(R.id.imageView_2s)
            val tv_1: TextView = dialog.findViewById(R.id.tv_1s)
            val tv_2: TextView = dialog.findViewById(R.id.tv_2s)
            val cl1s: CardView = dialog.findViewById(R.id.cl_1s)
            val cl2s: CardView = dialog.findViewById(R.id.cl_2s)
            // wakeup
             iv_wakeup_ = dialog.findViewById(R.id.iv_wakeup_s)
            val tc_wakeup_: TextView = dialog.findViewById(R.id.tc_wakeup_s)
            val cl3s: CardView = dialog.findViewById(R.id.cl3s)
            val constraintLayout19: ConstraintLayout = dialog.findViewById(R.id.constraintLayout19)

            // cancel alarm logic
            cancelAlarmLogic(cancelAlarm, cl1s, cl2s, cl3s, constraintLayout19, cl_cl)

            // close button
            val sleep_dialog_ttsclose: Button = dialog.findViewById(R.id.sleep_dialog_ttsclose)

            // close dialog
            sleep_dialog_ttsclose.setOnClickListener {
                dialog.dismiss()
            }

            // set sleep enhancer data
            tv_1.text = firstAlarm
            tv_2.text = secondAlarm
            Glide.with(requireActivity()).load(firstProgram).placeholder(R.drawable.alarm_off)
                .into(imageView_1)
            Glide.with(requireActivity()).load(secondProgram).placeholder(R.drawable.alarm_off)
                .into(imageView_2)

            // wakeup data
            setImageToDashboard(iv_wakeup_, wakeupTrait)
            tc_wakeup_.text = dat
            Glide.with(requireActivity()).load(dat_image).placeholder(R.drawable.alarm_off)
                .into(iv_wakeup_)

            // 30 sec hide main component
            Handler(Looper.getMainLooper()).postDelayed({
               cl_cl.visibility = View.GONE
                refreshbtn.visibility = View.GONE
                backDialogButton.visibility = View.VISIBLE
            }, 30000)

        }
        viewModel.getToSleepSave(genderSelected, durationSelected, programId, 1)

        if (!secondAlarm.equals("0")) {
            // set second audio alarm
            setAlarm((secondAlarm.toLong()) * 60 * 1000, audio2, 2)
        }

        if (!dat.equals("0:00")) {
            // set wakeup alarm
            setAlarmAtSpecificTime(dat, dat_url, 3)
        }
    }

    private fun cancelAlarmLogic(
        cancelAlarm: Button,
        cl1s: CardView,
        cl2s: CardView,
        cl3s: CardView,
        constraintLayout19: ConstraintLayout,
        cl_cl: ConstraintLayout
    ) {

        cl1s.setOnClickListener {
            manageCardSelection(1, cl1s, cl2s, cl3s, cancelAlarm)
        }

        cl2s.setOnClickListener {
            manageCardSelection(2, cl1s, cl2s, cl3s, cancelAlarm)
        }

        cl3s.setOnClickListener {
            manageCardSelection(3, cl1s, cl2s, cl3s, cancelAlarm)
        }

//        constraintLayout19.setOnClickListener {
//            cancelAlarm.visibility = View.GONE
//            resetCardBackgrounds(cl1s,cl2s, cl3s)
//        }
//
//        cl_cl.setOnClickListener {
//            cancelAlarm.visibility = View.GONE
//            resetCardBackgrounds(cl1s,cl2s, cl3s)
//        }

        cancelAlarm.setOnClickListener {
            if (selectedCards.isNotEmpty()) {
                // Cancel alarms for each selected card
                selectedCards.forEach { alarmId ->
                    cancelAlarm(alarmId)
                }

                // Clear the selected cards after canceling alarms
                selectedCards.clear()

                // Reset UI
                resetCardBackgrounds(cl1s, cl2s, cl3s)
                cancelAlarm.visibility = View.GONE
            } else {
                // Handle the case where no cards are selected (optional)
                Toast.makeText(requireContext(), "No alarms selected to cancel", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Function to manage card selection
    private fun manageCardSelection(cardIndex: Int,
                            cl1s: CardView,
                            cl2s: CardView,
                            cl3s: CardView,
                            cancelAlarm: Button) {
        if (selectedCards.contains(cardIndex)) {
            // Deselect the card
            selectedCards.remove(cardIndex)
        } else {
            // Select the card
            selectedCards.add(cardIndex)
        }

        Log.e("cards - ", "$selectedCards")

        // Update UI based on selected cards
        updateUI(cl1s, cl2s, cl3s, cancelAlarm)
    }

    // Function to update UI based on selected cards
    private fun updateUI( cl1s: CardView,
                  cl2s: CardView,
                  cl3s: CardView,
                  cancelAlarm: Button
    ) {
        // Reset card backgrounds
        resetCardBackgrounds(cl1s,cl2s, cl3s)

        // Set background color for selected cards
        selectedCards.forEach { cardIndex ->
            when (cardIndex) {
                1 -> cl1s.setCardBackgroundColor(Color.GRAY)
                2 -> cl2s.setCardBackgroundColor(Color.GRAY)
                3 -> cl3s.setCardBackgroundColor(Color.GRAY)
            }
        }

        // Show/hide cancelAlarm based on selected cards
        cancelAlarm.visibility = if (selectedCards.isNotEmpty()) View.VISIBLE else View.GONE
    }

    // Function to reset card backgrounds
    private fun resetCardBackgrounds(cl1s: CardView,
                             cl2s: CardView,
                             cl3s: CardView) {
        cl1s.setCardBackgroundColor(Color.parseColor("#121111"))
        cl2s.setCardBackgroundColor(Color.parseColor("#121111"))
        cl3s.setCardBackgroundColor(Color.parseColor("#121111"))
    }

    private fun cancelAlarm(alarmId: Int) {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarmId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun setImageToDashboard(imageView_3: ImageView, trait1: String) {
        when (trait1) {
            "O" -> {
                imageView_3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.setting_o
                    )
                )
            }

            "C" -> {
                imageView_3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.setting_c
                    )
                )
            }

            "E" -> {
                imageView_3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.setting_e
                    )
                )
            }

            "A" -> {
                imageView_3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.setting_a
                    )
                )
            }

            "N" -> {
                imageView_3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.setting_n
                    )
                )
            }

            else -> {
                imageView_3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.logo_toolbar
                    )
                )
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
        val transitionToSleepAdapter =
            NewAdapterGetToSleep(requireActivity(), this, viewModel.getToSleepProgramList())
        binding.transitionToSleepRecycler.setHasFixedSize(true)
        binding.transitionToSleepRecycler.layoutManager = GridLayoutManager(context, 2)
        binding.transitionToSleepRecycler.adapter = transitionToSleepAdapter


    }

    private fun videoPlay() {
        val mediaController = MediaController(context)
        mediaController.setMediaPlayer(binding.videoView)
        val uri =
            Uri.parse("android.resource://" + context?.packageName + "/R.raw/" + R.raw.transitionvidnew)
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
        Log.e("er", "" + programId)
        viewModel.getToSleepVideoList(genderSelected, 5, programId)
    }

    private fun selectedFemale() {
        binding.sleepMale.setTextColor(Color.GRAY)
        binding.sleepFemale.setTextColor(Color.WHITE)
        genderSelected = 4
    }

    private fun selectedMale() {
        binding.sleepMale.setTextColor(Color.WHITE)
        binding.sleepFemale.setTextColor(Color.GRAY)
        genderSelected = 3
    }

    private fun selected5Mint() {
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
    private fun setAlarm(delay: Long, audioUrl: String, alarmNumber: Int) {

        if (alarmNumber == 1 || alarmNumber == 2) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("URL_EXTRA", audioUrl) // Pass the URL as an extra parameter
                putExtra("alarmNumber", "" + alarmNumber) // Pass the URL as an extra parameter
                putExtra(
                    "userId",
                    "" + repository.getAuthData()?.id.toString()
                ) // Pass the URL as an extra parameter

            } // Create a new BroadcastReceiver class for handling alarms
            val requestCode = alarmNumber
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val currentTime = System.currentTimeMillis()
            val triggerTime = currentTime + delay

            alarmManager.cancel(pendingIntent)

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarmAtSpecificTime(time: String, audioUrl: String, alarmNumber: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Parse the time string to get hours and minutes
        val timeParts = time.split(":")
        val hours = timeParts[0].toInt()
        val minutes = timeParts[1].toInt()

        // Get the current date and time
        val calendar = Calendar.getInstance()
        val currentTimeMillis = System.currentTimeMillis()
        calendar.timeInMillis = currentTimeMillis

        // Set the alarm time to the specified hours and minutes
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)

        // Check if the alarm time is in the past, if yes, set it for the next day
        if (calendar.timeInMillis <= currentTimeMillis) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Create an intent for the BroadcastReceiver
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("URL_EXTRA", audioUrl)
            putExtra("alarmNumber", "" + alarmNumber)
            putExtra(
                "userId",
                "" + repository.getAuthData()?.id.toString()
            ) // Pass the URL as an extra parameter

        }

        val commonObject = CommonObject()
        commonObject.setFragmentManager(requireActivity().supportFragmentManager)

        CommonObject.fragmentManagerContext = requireActivity().supportFragmentManager

        val requestCode = 3
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)

        // Set the alarm using AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        sleepAlarmDataSet("wake_up")
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
        val customProgressDialog = CustomProgressDialog(requireContext())
        customProgressDialog.showSweetDialog()

        val url = "https://app.dialupdelta.com/web/api/change_language"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        // Form data
        val uid = repository.getAuthData()?.id.toString()
        val languageId = repository.getLanguage().toString()

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                customProgressDialog.dismissSweet()
                // Handle the response here
                Log.e("Response language: ", response + " - " + uid + " - " + languageId)

                viewModel = ViewModelProvider(this, factory)[GetToSleepViewModel::class.java]
                setObserver(viewModel)
                viewModel.getToSleepList()
                viewModel.saveGetToSleepApi()
                viewModel.savedSleepEnhancer()
                viewModel.fetchWakeUpSaved()
            },
            Response.ErrorListener { error ->
                customProgressDialog.dismissSweet()
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

    fun sleepAlarmDataSet(module: String) {

        val customProgressDialog = CustomProgressDialog(requireContext())
        customProgressDialog.showSweetDialog()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are zero-based, so add 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateCurrent = "$year-$month-$day"

        var time = "0"
        if (module.equals("sleep_enhancer")) {
            time = firstAlarm
        } else {
            time = dat
        }

        val url = "https://app.dialupdelta.com/web/api/save_sleep_activity"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        // Form data
        val uid = repository.getAuthData()?.id.toString()

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                customProgressDialog.dismissSweet()
                // Handle the response here
                Log.e("save sleep activity: ", response + " - " + uid + "-   $module")

                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getBoolean("status");
                    if (status) {
                        Log.e("sleep data ", jsonObject.getJSONObject("result").toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            },
            Response.ErrorListener { error ->
                customProgressDialog.dismissSweet()
                // Handle error here
                println("Error: $error")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = uid
                params["time_for"] = "sleep"
                params["recorded_time"] = time
                params["activity_on"] = dateCurrent
                params["module_name"] = module
                params["module_id"] = "1"
                return params
            }
        }

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
    }

    override fun playedAlarm(alarmPlayedNumber: String?) {
        if (alarmPlayedNumber != null) {
            Log.e("alarmNumber-", alarmPlayedNumber)
        }
        if(alarmPlayedNumber.equals("1")){
            Glide.with(requireActivity()).load(firstProgram).placeholder(R.drawable.alarm_off)
                .into(imageView_1)
            Glide.with(requireActivity()).load(firstProgram).placeholder(R.drawable.alarm_off)
                .into(imageView1)
        }else if(alarmPlayedNumber.equals("2")){
            Glide.with(requireActivity()).load(secondProgram).placeholder(R.drawable.alarm_off)
                .into(imageView_2)
            Glide.with(requireActivity()).load(secondProgram).placeholder(R.drawable.alarm_off)
                .into(imageView2)
        }else if(alarmPlayedNumber.equals("3")){
            Glide.with(requireActivity()).load(secondProgram).placeholder(R.drawable.alarm_off)
                .into(iv_wakeup_)
            Glide.with(requireActivity()).load(secondProgram).placeholder(R.drawable.alarm_off)
                .into(ivWakeup)
        }
    }

    fun getMp3() {
        val url = "https://app.dialupdelta.com/web/api/get_dud_songs"

        // Create a POST request using Volley
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                // Handle response from the server
                // You can parse the response if needed
                Log.e("Response mp3", "$response")
                val jsonObject = JSONObject(response);
                if (jsonObject.getBoolean("status")) {

                val gson = Gson()
                val mp3response = gson.fromJson(response, DudSongsResponse::class.java)

                val mp3Recycler = Mp3Recycler(requireActivity(), mp3response.result.data, this, mp3response.result.base_url)
                binding.mp3Recycler.setHasFixedSize(true)
                binding.mp3Recycler.layoutManager = GridLayoutManager(context, 2)
                binding.mp3Recycler.adapter = mp3Recycler

            }

            },
            Response.ErrorListener { error ->
                // Handle errors
                println("Error: $error")
            }) {
            override fun getParams(): MutableMap<String, String> {
                return params
            }
        }

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest)
    }

    override fun getClickedNumber(number: Int, url: String?) {
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
        val customDialog = CustomProgressDialog(requireContext())
        customDialog.showSweetDialog()

        val  uriAudioUrl = Uri.parse(url)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()
//                mediaPlayer.release()
                mediaPlayer.setDataSource(requireActivity(), uriAudioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    it.start()
                    customDialog.dismissSweet()
                }
            }else {
                mediaPlayer.setDataSource(requireActivity(), uriAudioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    it.start()
                    customDialog.dismissSweet()
                }
            }

            binding.bgcMCl.visibility = View.VISIBLE
            binding.loaderProgressBar.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getToSleepAudioProgram() {
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.reset()
        }

//        val videoBaseUrl = viewModel.getToSleepVideoData()?.base_url_video
        val videoSubUrl = viewModel.getToSleepVideoData()?.list?.get(0)?.video_url
        val videoBaseUrl = "https://app.dialupdelta.com/uploads/mp3/"
        val completeVideoUrl = "$videoBaseUrl/$videoSubUrl"

//        Toast.makeText(requireContext(), "$completeVideoUrl", Toast.LENGTH_LONG).show()

        val customDialog = CustomProgressDialog(requireContext())
        customDialog.showSweetDialog()

        val  uriAudioUrl = Uri.parse(completeVideoUrl)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()
//                mediaPlayer.release()
                mediaPlayer.setDataSource(requireActivity(), uriAudioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    it.start()
                    customDialog.dismissSweet()
                }
            }else {
                mediaPlayer.setDataSource(requireActivity(), uriAudioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    it.start()
                    customDialog.dismissSweet()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        customDialog.dismissSweet()
    }

}