package com.dialupdelta.ui.wakeup

import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.`interface`.ClickInterface
import com.dialupdelta.`interface`.LongPressSleep2
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentWakeUpBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class WakeUpFragment : BaseFragment(), ClickInterface, LongPressSleep2 {
    private lateinit var binding:FragmentWakeUpBinding
    private lateinit var recyclerView: RecyclerView
    private var id1Male: MutableList<String> = ArrayList()
    private var fileURL: MutableList<String> = ArrayList()
    private var thumb: MutableList<String> = ArrayList()
    private var durationL: MutableList<String> = ArrayList()
    private var traits: MutableList<String> = ArrayList()
    private var fileName: MutableList<String> = ArrayList()
    private var id45: MutableList<String> = ArrayList()
    private var traint45: MutableList<String> = ArrayList()
    private var duration45: MutableList<String> = ArrayList()
    private var thumb45: MutableList<String> = ArrayList()
    private var url45: MutableList<String> = ArrayList()
    private lateinit var playerView1: PlayerView
    var player: SimpleExoPlayer? = null
    var newUrl: String = "0"
    var isFull: String = ""
    lateinit var closeID: ImageView
    var timeNew: Int = 5
    var choosedTraits = "O"
    private var clickedUrl = "0"
    private var videoType = "sub"
    private var chooseWakeupVideotime = ""
    private var chooseRepeatDays = ""
    private var clickedImageUrl = ""
    private var day = 0
    private var month: Int = 0
    private var year: Int = 0
    private val sec = 0
    private var hours = 0
    private var minutes = 0
    private var choosedProgram = ""
    private var choosedUrl = ""
    lateinit var exo_fullscreen_icon: ImageView
    lateinit var exo_fullscreen_icon_tts: ImageView
    private var clickedVideoTime = "45sec"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wake_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        videoPlay()
        //exo_fullscreen_icon = findViewById(R.id.exo_fullscreen_icon)
        binding.timeTv.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val mHour = calendar.get(Calendar.HOUR_OF_DAY)
            val mMinute = calendar.get(Calendar.MINUTE)
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                binding.timeTv.text = SimpleDateFormat("HH:mm").format(calendar.time)
                var timeX = SimpleDateFormat("HH:mm").format(calendar.time)
//                timeNew = Integer.parseInt(timeX)
                hours = hour
                minutes = minute
                timeNew = toMins(timeX)

            }
            binding.timeTv.setOnClickListener {
                TimePickerDialog.THEME_DEVICE_DEFAULT_DARK

                TimePickerDialog(
                    context, R.style.MyTimePickerDialogTheme, timeSetListener, calendar.get(
                        Calendar.HOUR_OF_DAY
                    ), calendar.get(Calendar.MINUTE), true
                ).show()
            }
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

                val dialog_ok: TextView = dialog.findViewById<TextView>(R.id.dialog_ok)
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
    }

    private fun setData() {
        id1Male.add("1")
        fileName.add("Program1")
        traits.add("")
        durationL.add("")
        thumb.add("https://app.whyuru.com/assets/uploads/wakeUp/screen_1665407723.png")
        fileURL.add("Program 1")

        id1Male.add("2")
        fileName.add("Program2")
        traits.add("")
        durationL.add("")
        thumb.add("https://app.whyuru.com/assets/uploads/wakeUp/screen_1665407868.png")
        fileURL.add("Program 2")

        id1Male.add("3")
        fileName.add("Program3")
        traits.add("")
        durationL.add("")
        thumb.add("https://app.whyuru.com/assets/uploads/wakeUp/screen_1665407774.png")
        fileURL.add("Program 3")

        id1Male.add("4")
        fileName.add("Program4")
        traits.add("")
        durationL.add("")
        thumb.add("https://app.whyuru.com/assets/uploads/wakeUp/screen_1665407845.png")
        fileURL.add("Program 4")
       // adapterConnects()
    }

    private fun toMins(s: String): Int {
        val hourMin: Array<String> = s.split(":").toTypedArray()
        val hour: Int = hourMin[0].toInt()
        val mins: Int = hourMin[1].toInt()
        val hoursInMins = hour * 60
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
                "o")

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
                chooseRepeatDays = showval
            }
        }

        monDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                monDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "mon"
                } else {
                    "$showval1,mon "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval
            }
        }

        tuesDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tuesDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "tue"
                } else {
                    "$showval1,tue "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval
            }
        }

        wedsnesDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                wedsnesDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "wed"
                } else {
                    "$showval1,wed "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval
            }
        }

        thursDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                thursDay.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "thu"
                } else {
                    "$showval1,thu "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval
            }
        }

        friday4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                friday4.isChecked = true
                never.isChecked = false

                showval1 = if (showval1 == "") {
                    "fri"
                } else {
                    "$showval1,fri "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval
            }
        }

        saturDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                saturDay.isChecked = true
                never.isChecked = false
                showval1 = if (showval1 == "") {
                    "sat"
                } else {
                    "$showval1,sat "
                }

                dayOfAlarm.text = showval1
                chooseRepeatDays = showval
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
}