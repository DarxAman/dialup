package com.dialupdelta.ui.wakeup

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.`interface`.ClickInterface
import com.dialupdelta.`interface`.GetToSleepClickListener
import com.dialupdelta.`interface`.LongPressSleep2
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.data.network.response.wake_up_response.WakeUp
import com.dialupdelta.databinding.FragmentWakeUpBinding
import com.dialupdelta.utils.setGone
import com.dialupdelta.utils.setUserImage
import com.dialupdelta.utils.setVisible
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import de.hdodenhof.circleimageview.CircleImageView
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class WakeUpFragment : BaseFragment(), ClickInterface, LongPressSleep2, GetToSleepClickListener {
    private lateinit var binding: FragmentWakeUpBinding
    private lateinit var recyclerView: RecyclerView
    private var id45: MutableList<String> = ArrayList()
    private var traint45: MutableList<String> = ArrayList()
    private var duration45: MutableList<String> = ArrayList()
    private var thumb45: MutableList<String> = ArrayList()
    private var url45: MutableList<String> = ArrayList()
    private lateinit var playerView1: PlayerView
    var player: SimpleExoPlayer? = null
    var newUrl: String = "0"
    lateinit var closeID: ImageView
    var timeNew: Int = 5
    private var chooseRepeatDays = "never"
    private var day = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hours = 0
    private var minutes = 0
    private val localWakeUpSaveData = LocalWakeUpSaveData()
    lateinit var exo_fullscreen_icon: ImageView
    private var clickedVideoTime = "45sec"
    private var choosedTime = "0:00"
    private val factory: WakeUpViewModelFactory by instance()
    private lateinit var viewModel: WakeUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wake_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[WakeUpViewModel::class.java]
        videoPlay()
        setObserver(viewModel)

        viewModel.getWakeProgramList()

        viewModel.fetchWakeUpSaved()

        //exo_fullscreen_icon = findViewById(R.id.exo_fullscreen_icon)

        binding.timeTv.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            // Inflate the custom layout for the TimePickerDialog
            val dialogView =
                View.inflate(requireContext(), R.layout.custom_time_picker_dialog, null)
            val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)

            // Create a custom dialog and set its view to the custom layout
            val timePickerDialog = AlertDialog.Builder(
                requireContext(),
                R.style.CustomTimePickerDialogStyle // Use the custom style for the AlertDialog
            )
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    // Get the selected time from the TimePicker
                    val hour = timePicker.currentHour
                    val minute = timePicker.currentMinute

                    // Update the TextView with the selected time
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)

                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val selectedTime = timeFormat.format(calendar.time)
                    binding.timeTv.text = selectedTime
                    choosedTime = selectedTime
                }
                .setNegativeButton("Cancel", null)
                .create()

            // Show the custom TimePickerDialog
            timePickerDialog.show()
        }

        binding.dayOfAlarm.setOnClickListener {
            val dialog = Dialog(requireContext())
            if (dialog != null) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.dialogshow_options)
                dialog.show()

                var back_dialog_wake: ImageView = dialog.findViewById(R.id.back_dialog_wake)
                back_dialog_wake.setOnClickListener {
                    dialog.dismiss()
                }

                val dialog_ok: TextView = dialog.findViewById(R.id.dialog_ok)
                dialog_ok.setOnClickListener {
                    dialog.dismiss()
                }

//                val recyclerView: RecyclerView = dialog.findViewById(R.id.recyclerNewSleep);

                var showval = ""

                val friday4: CheckBox = dialog.findViewById(R.id.friCheck)
                val sunday: CheckBox = dialog.findViewById(R.id.sunCheck)
                val monDay: CheckBox = dialog.findViewById(R.id.monCheck)
                val tuesDay: CheckBox = dialog.findViewById(R.id.tueCheck)
                val wedsnesDay: CheckBox = dialog.findViewById(R.id.wedCheck)
                val thursDay: CheckBox = dialog.findViewById(R.id.thuCheck)
                val saturDay: CheckBox = dialog.findViewById(R.id.satCheck)
                val everyDay: CheckBox = dialog.findViewById(R.id.everyCheck)
                val never: CheckBox = dialog.findViewById(R.id.neverCheck)

                checkClickFunctionality(
                    everyDay,
                    monDay,
                    tuesDay,
                    wedsnesDay,
                    thursDay,
                    friday4,
                    saturDay,
                    sunday,
                    never,
                    binding.dayOfAlarm,
                    showval
                )
            }
        }

        binding.male.setOnClickListener {
            selectedMale()
        }

        binding.female.setOnClickListener {
            selectedFemale()
        }

        binding.saveWakeup.setOnClickListener {

            if (chooseRepeatDays.isNotEmpty() && choosedTime.isNotEmpty()) {
                localWakeUpSaveData.thumbUrl = viewModel.getWakeUpThumbData()
                localWakeUpSaveData.videoUrl = viewModel.getWakeUpVideoData()
                localWakeUpSaveData.repeatDays = chooseRepeatDays
                localWakeUpSaveData.time = choosedTime
                viewModel.wakeUpSaver(localWakeUpSaveData)

                Toast.makeText(
                    context,
                    "Alarm saved, please set it from Start Timer",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Time and Days are not set", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toMins(s: String): Int {
        val hourMin: Array<String> = s.split(":").toTypedArray()
        val hour: Int = hourMin[0].toInt()
        val mins: Int = hourMin[1].toInt()
        val hoursInMins = hour * 60
        localWakeUpSaveData.time = "${hoursInMins + mins}"
        return hoursInMins + mins
    }

    private fun videoPlay() {
        val mediaController = MediaController(context)
        mediaController.setMediaPlayer(binding.wakeUpVideo)
        val uri =
            Uri.parse("android.resource://" + context?.packageName + "/R.raw/" + R.raw.moonset)
        binding.wakeUpVideo.setVideoURI(uri)
        binding.wakeUpVideo.start()
        binding.wakeUpVideo.setOnPreparedListener { mp -> mp.isLooping = true }

    }

    private fun showDialog(title: String, gender: String) {
        val dialog = context?.let { Dialog(it, android.R.style.Theme_Holo_Light) }
        if (dialog != null) {
            var traits = "O"
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialogtransition)
            dialog.show()
            recyclerView = dialog.findViewById(R.id.recyclerNewSleep)

            //  apiVideosLast("45sec", title)
            val adapterSleep1 = AdapterSleep(
                requireActivity(),
                id45,
                traint45,
                thumb45,
                duration45,
                url45,
                this,
                this,
                "o"
            )
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = adapterSleep1


            val dialog_title: TextView = dialog.findViewById(R.id.dialog_title)
            dialog_title.text = title

            playerView1 = dialog.findViewById(R.id.playerView1)

            val clExoPlayer = dialog.findViewById(R.id.clExoPlayer) as ConstraintLayout
            val closebtndialog1 = dialog.findViewById(R.id.closebtndialog1) as CircleImageView
            closebtndialog1.setOnClickListener {
                clExoPlayer.visibility = View.INVISIBLE
                playerView1.onPause()
                playerView1.setKeepContentOnPlayerReset(true)
                player?.pause()
//               playerView11.onPause()
//                playerView11.visibility = View.INVISIBLE

                exo_fullscreen_icon.visibility = View.INVISIBLE
                closeID.visibility = View.INVISIBLE
            }
            // click()
            val logo: ImageView = dialog.findViewById(R.id.logo)

            when (title) {
                "E" -> {
                    traits = "E"
                    logo.setImageResource(R.drawable.setting_e)
                    dialog_title.setTextColor(Color.parseColor("#FF0000"))
                    // apiVideosLast("45dec", "E")

                }

                "A" -> {
                    traits = "A"
                    logo.setImageResource(R.drawable.setting_a)
                    dialog_title.setTextColor(Color.parseColor("#F9CA14"))
                    // apiVideosLast("45dec", "A")
                }

                "N" -> {
                    traits = "N"
                    logo.setImageResource(R.drawable.setting_n)
                    dialog_title.setTextColor(Color.parseColor("#808080"))
                    //  apiVideosLast("45dec", "N")

                }

                "O" -> {
                    traits = "O"
                    logo.setImageResource(R.drawable.setting_o)
                    dialog_title.setTextColor(Color.parseColor("#008000"))

                    //apiVideosLast("45dec", "O")
                }

                "C" -> {
                    traits = "C"
                    logo.setImageResource(R.drawable.setting_c)
                    dialog_title.setTextColor(Color.parseColor("#0000FF"))
                    //   apiVideosLast("45dec", "C")

                }
            }
//            logo.setImageDrawable(iview)

            val cl90: ConstraintLayout = dialog.findViewById(R.id.cl90)
            val cl45: ConstraintLayout = dialog.findViewById(R.id.cl45)

            val tv45: TextView = dialog.findViewById(R.id.textView7)
            val tv90: TextView = dialog.findViewById(R.id.program)

            val adapterSleep = AdapterSleep(
                requireActivity(),
                id45,
                traint45,
                thumb45,
                duration45,
                url45,
                this,
                this,
                "o"
            )

            cl45.setOnClickListener {
                clickedVideoTime = "45sec"
                // apiVideosLast("45dec", traits)
                tv45.setTextColor(Color.parseColor("#008000"))
                tv90.setTextColor(Color.BLUE)

            }

            cl90.setOnClickListener {
                clickedVideoTime = "90sec"
                tv90.setTextColor(Color.parseColor("#008000"))
                tv45.setTextColor(Color.BLUE)
                //  apiVideosLast("90sec", traits)
            }

            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = adapterSleep


            val closebtndialog = dialog.findViewById<ImageView>(R.id.closebtndialog)
            closebtndialog.setOnClickListener {
                dialog.dismiss()
                player?.pause()
            }
        }
    }

    private fun checkClickFunctionality(
        everyDay: CheckBox,
        monDay: CheckBox,
        tuesDay: CheckBox,
        wedsnesDay: CheckBox,
        thursDay: CheckBox,
        friday4: CheckBox,
        saturDay: CheckBox,
        sunday: CheckBox,
        never: CheckBox,
        dayOfAlarm: TextView,
        showval: String
    ) {
        var showval1 = showval
        everyDay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                monDay.isChecked = true
                tuesDay.isChecked = true
                wedsnesDay.isChecked = true
                thursDay.isChecked = true
                friday4.isChecked = true
                saturDay.isChecked = true
                sunday.isChecked = true
                everyDay.isChecked = true
                never.isChecked = false

                dayOfAlarm.text = "everyday"
                chooseRepeatDays = "everyday"
            }
        }

        never.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                monDay.isChecked = false
                tuesDay.isChecked = false
                wedsnesDay.isChecked = false
                thursDay.isChecked = false
                friday4.isChecked = false
                saturDay.isChecked = false
                sunday.isChecked = false
                everyDay.isChecked = false
                never.isChecked = true
                dayOfAlarm.text = "never"
                chooseRepeatDays = "never"
            }
        }

        sunday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sunday.isChecked = true
                never.isChecked = false
                showval1 = if (showval1 == "") {
                    "sun  "
                } else {
                    "$showval1,sun "
                }
                dayOfAlarm.text = showval1
                chooseRepeatDays = showval1
            }
        }

        monDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                monDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "mon"
                } else {
                    "$showval1, mon "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval1
            }
        }

        tuesDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tuesDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "tue"
                } else {
                    "$showval1, tue "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval1
            }
        }

        wedsnesDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                wedsnesDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "wed"
                } else {
                    "$showval1, wed "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval1
            }
        }

        thursDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                thursDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "thu"
                } else {
                    "$showval1, thu "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval1
            }
        }

        friday4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                friday4.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "fri"
                } else {
                    "$showval1, fri "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval1
            }
        }

        saturDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                saturDay.isChecked = true
                never.isChecked = false
                showval1 = if (showval1 == "") {
                    "sat"
                } else {
                    "$showval1, sat "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval1
            }
        }
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

    override fun longPressId(urlLong: String?, trait: String?) {

    }

    override fun urlGet(url: String?, clickImageUrl: String?) {

    }

    override fun getToSleepProgramItemListener(position: Int) {
        localWakeUpSaveData.program = viewModel.getWakeUpProgramList()?.get(position)?.id
        viewModel.getWakeList(localWakeUpSaveData.gender, localWakeUpSaveData.program)
    }

    private fun setObserver(viewModel: WakeUpViewModel) {

        viewModel.successWakeUpProgram.observe(viewLifecycleOwner) {
            val adapter = WakeUpAdapter(requireContext(), this, viewModel.getWakeUpProgramList())
            binding.wakeUpRecyclerView.layoutManager = GridLayoutManager(context, 2)
            binding.wakeUpRecyclerView.adapter = adapter
        }

        viewModel.wakeUpProgramResponse.observe(viewLifecycleOwner) {
            if (it?.list?.isEmpty() == false) {
                openWakeUpDialog(it)
            }
        }

        viewModel.successWakeUp.observe(viewLifecycleOwner) {
            if (it.gender == 3) {
                selectedMale()
            } else {
                selectedFemale()
            }

            binding.dayOfAlarm.text = it?.repeatDays ?: "never"
            chooseRepeatDays = it.repeatDays

            binding.timeTv.text = it?.time ?: "0:00"
            choosedTime = it.time
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openWakeUpDialog(wakeUp: WakeUp) {
        val dialog = context?.let { Dialog(it, android.R.style.Theme_Holo_Light) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialogwakeupandevening)

            val closeBtnDialog = dialog.findViewById(R.id.canceldialog) as ImageView
            val firstImage = dialog.findViewById(R.id.firstImage) as ImageView
            val secondImage = dialog.findViewById(R.id.secondImage) as ImageView
            val thirdImage = dialog.findViewById(R.id.thirdImage) as ImageView
            val mainImage = dialog.findViewById(R.id.mainImage) as ImageView
            val videoPlay = dialog.findViewById(R.id.videoPlay) as PlayerView
            val button_set1 = dialog.findViewById(R.id.button_set1) as Button
            val button_set2 = dialog.findViewById(R.id.button_set2) as Button
            val button_set3 = dialog.findViewById(R.id.button_set3) as Button


            if (wakeUp.list.size >= 1) {
                firstImage.setUserImage(
                    requireContext(),
                    wakeUp.base_url_image + wakeUp.list[0].thumb1
                )
                secondImage.setUserImage(
                    requireContext(),
                    wakeUp.base_url_image + wakeUp.list[0].thumb2
                )
                thirdImage.setUserImage(
                    requireContext(),
                    wakeUp.base_url_image + wakeUp.list[0].thumb3
                )
                mainImage.setUserImage(
                    requireContext(),
                    wakeUp.base_url_image + wakeUp.list[0].main_thumb
                )
            }

            videoPlay.setGone()

            var position: Int
            player = SimpleExoPlayer.Builder(requireActivity()).build()

            firstImage.setOnClickListener {
                position = 0
                dialogVideoPlay(videoPlay, wakeUp, position, closeBtnDialog, dialog, player!!)
            }

            secondImage.setOnClickListener {
                position = 1
                dialogVideoPlay(videoPlay, wakeUp, position, closeBtnDialog, dialog, player!!)
            }

            thirdImage.setOnClickListener {
                position = 2
                dialogVideoPlay(videoPlay, wakeUp, position, closeBtnDialog, dialog, player!!)
            }

            mainImage.setOnClickListener {
                position = 3
                dialogVideoPlay(videoPlay, wakeUp, position, closeBtnDialog, dialog, player!!)
            }

            button_set1.setOnClickListener {
                val videoSubUrl = wakeUp.list[0].sub1Url
                val completeVideoUrl = "${wakeUp.base_url_video}/$videoSubUrl"
                val thumbSubUrl = wakeUp.list[0].thumb1
                val completeThumbUrl = "${wakeUp.base_url_image}/$thumbSubUrl"
                viewModel.setWakeUpThumbData(completeThumbUrl)
                viewModel.setWakeUpVideoData(completeVideoUrl)
                Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
                true
            }

            button_set2.setOnClickListener {
                val videoSubUrl = wakeUp.list[0].sub2Url
                val completeVideoUrl = "${wakeUp.base_url_video}/$videoSubUrl"
                viewModel.setWakeUpVideoData(completeVideoUrl)

                val thumbSubUrl = wakeUp.list[0].thumb2
                val completeThumbUrl = "${wakeUp.base_url_image}/$thumbSubUrl"
                viewModel.setWakeUpThumbData(completeThumbUrl)

                Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
                true
            }

            button_set3.setOnClickListener {
                val videoSubUrl = wakeUp.list[0].sub3Url
                val completeVideoUrl = "${wakeUp.base_url_video}/$videoSubUrl"
                viewModel.setWakeUpVideoData(completeVideoUrl)

                val thumbSubUrl = wakeUp.list[0].thumb3
                val completeThumbUrl = "${wakeUp.base_url_image}/$thumbSubUrl"
                viewModel.setWakeUpThumbData(completeThumbUrl)

                Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
                true
            }

            dialog.show()
        }
    }

    private fun dialogVideoPlay(
        videoPlay: PlayerView,
        wakeUp: WakeUp,
        position: Int,
        closeBtnDialog: ImageView,
        dialog: Dialog,
        player: SimpleExoPlayer
    ) {
        videoPlay.setVisible()
        var videoSubUrl = ""
        if (position == 0) {
            videoSubUrl = wakeUp.list[0].sub1Url
        } else if (position == 1) {
            videoSubUrl = wakeUp.list[0].sub2Url
        } else if (position == 2) {
            videoSubUrl = wakeUp.list[0].sub3Url
        } else {
            videoSubUrl = wakeUp.list[0].main_video
        }

        val completeVideoUrl =
            "https://sample-videos.com/video321/mp4/240/big_buck_bunny_240p_30mb.mp4"
        val mediaItem: MediaItem = completeVideoUrl.let { MediaItem.fromUri(it) }

        if (player!!.isPlaying) {
            player!!.pause()
            player!!.stop()
        }
        videoPlay.player = this.player
        player!!.setMediaItem(mediaItem)
        player!!.prepare()
        player!!.play()
        player?.volume = 10f

        closeBtnDialog.setOnClickListener {
            player?.pause()
            player?.stop()
            dialog.dismiss()
        }
    }

    private fun selectedFemale() {
        binding.male.setTextColor(Color.GRAY)
        binding.female.setTextColor(Color.WHITE)
        localWakeUpSaveData.gender = 4
    }

    private fun selectedMale() {
        binding.male.setTextColor(Color.WHITE)
        binding.female.setTextColor(Color.GRAY)
        localWakeUpSaveData.gender = 3
    }
}