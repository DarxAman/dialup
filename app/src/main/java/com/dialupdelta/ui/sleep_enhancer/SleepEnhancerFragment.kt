package com.dialupdelta.ui.sleep_enhancer

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.appcompat.widget.ViewUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.dialupdelta.R
import com.dialupdelta.`interface`.ProgramListListener
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.data.preferences.PreferenceProvider
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.databinding.FragmentSleepEnhancerBinding
import com.dialupdelta.ui.sleep_enhancer.adapter.SleepEnhancerDialogAdapter
import com.dialupdelta.ui.sleep_enhancer.adapter.SleepEnhancerProgramListAdapter
import com.dialupdelta.utils.CustomProgressDialog
import com.dialupdelta.utils.setGone
import com.dialupdelta.utils.setVisible
import com.dialupdelta.utils.showToastMessage
import org.json.JSONObject
import org.kodein.di.generic.instance

class SleepEnhancerFragment : BaseFragment(), ProgramListListener {
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
    private var currentVolume = 0
    private var showSetAlarmLeft = 0
    private var showSetAlarmRight = 0
    private val localSaveSleepEnhancer = LocalSaveSleepEnhancer()
//    private val prefs = PreferenceProvider(this)
    private lateinit var recyclerNewSleep:RecyclerView
    private lateinit var progressBar: ProgressBar
    private val factory: SleepEnhancerViewModelFactory by instance()
    private lateinit var viewModel: SleepEnhancerViewModel
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var program1 = ""
    private var program2 = ""
    private var selectedThumb = ""
    private var selectedAlarmProgram = 0
    private var currentPosition = 0
    private val repository: Repository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_enhancer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        showOptions()
        showOptionRight()

        viewModel = ViewModelProvider(this, factory)[SleepEnhancerViewModel::class.java]
        setObserver(viewModel)
//        hideOptions()
//        hideOptionRight()
        viewModel.getSleepEnhancerProgramList()

        viewModel.savedSleepEnhancer()
        makeApiRequest()

        localSaveSleepEnhancer.time1 = answerForLeft.toString()
        localSaveSleepEnhancer.time2 = answerForRight.toString()

        binding.seekBar111.max = 100
        binding.seekBar111.progress = currentVolume

        binding.resetBtn.setOnClickListener {
            hideOptions()
            hideOptionRight()
            answerForLeft = 45
            answerForRight = 135
            binding.showOptionCLickRight.visibility = View.VISIBLE
            binding.showOptionclick.visibility = View.VISIBLE
        }

        binding.bottom1.setOnClickListener {

            showProgramDialog("First Alarm", 1)
        }

        binding.bottom2.setOnClickListener {
            showProgramDialog("Second Alarm", 2)
        }

        binding.saveSleepEnhancer.setOnClickListener {
            val time1 = answerForLeft.toString()
            val time2 = answerForRight.toString()
            val userId = viewModel.getAuthData()?.id.toString()

            if (time1.isNotEmpty() && time2.isNotEmpty() && userId.isNotEmpty()
                ) {

                localSaveSleepEnhancer.time1 = time1
                localSaveSleepEnhancer.time2 = time2
                localSaveSleepEnhancer.user_id = userId
                viewModel.sleepEnhancerSaver(localSaveSleepEnhancer)

                Toast.makeText(context, "Alarm saved, please set it from Start Timer", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(context, "Not all values set", Toast.LENGTH_SHORT).show()

//                Log.e("check", time1 + " - " + time2 + " dur - " + duration1 + " - " + duration2
//                    + " prg - " + program1 + " - " + program2 + "al - " + localSaveSleepEnhancer.audio_1 + localSaveSleepEnhancer.audio_2)
            }
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
                    localSaveSleepEnhancer.volume = progress.toString()
                        binding.seekBar111.progress = progress
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

    private fun showProgramDialog(alarmSelected : String, alarmNumber : Int) {

        selectedAlarmProgram = alarmNumber

        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.sleep_program_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val dialogTitle = dialog.findViewById(R.id.dialog_title_program) as TextView
        dialogTitle.text = alarmSelected

        val logo = dialog.findViewById(R.id.logo_program) as ImageView

        progressBar = dialog.findViewById(R.id.progressBar) as ProgressBar
        recyclerNewSleep = dialog.findViewById(R.id.recyclerSleepPrograms) as RecyclerView

        viewModel.successProgramList.observe(viewLifecycleOwner){
            val adapter = SleepEnhancerProgramListAdapter(requireContext(), viewModel.getSleepProgramList(), this)
            recyclerNewSleep.layoutManager = GridLayoutManager(requireContext(),2)
            recyclerNewSleep.adapter = adapter
        }


        val closeBtnDialog = dialog.findViewById(R.id.closebtndialog_sleep_program) as ImageView
        closeBtnDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver(viewModel: SleepEnhancerViewModel) {

        viewModel.audioDataList.observe(viewLifecycleOwner){
            progressBar.setGone()
            if (it.list.isNotEmpty()){
                recyclerNewSleep.adapter?.notifyDataSetChanged()
                val adapter = SleepEnhancerDialogAdapter(requireActivity(),this, viewModel.getSleepAudioList()?.list)
                recyclerNewSleep.layoutManager = GridLayoutManager(requireActivity(), 1)
                recyclerNewSleep.adapter = adapter
            }
        }

        viewModel.noDataFound.observe(viewLifecycleOwner){
            progressBar.setGone()
            recyclerNewSleep.adapter?.notifyDataSetChanged()
           showToastMessage(requireContext(), "No Data Found")
        }

        viewModel.durationTime.observe(viewLifecycleOwner){
//            getDurationProgram(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
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
                localSaveSleepEnhancer.time2 = answerForRight.toString()
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
                localSaveSleepEnhancer.time1 = answerForLeft.toString()
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

    override fun setOnAudioClickListener(position: Int) {
        sleepEnhancerPlayAudio(position)
    }

    // audio choose button click listener
    override fun setOnAudioLongClickListener(position: Int) {

        val selectedImage = viewModel.getSleepProgramList()?.get(currentPosition)?.thumb.toString()

        val audioBaseUrl = viewModel.getSleepAudioList()?.base_url_mp3
        val audioSubUrl = viewModel.getSleepAudioList()?.list?.get(position)?.file_name
        val completeUrl = audioBaseUrl+audioSubUrl

        // save audio url tp the local storage
        if (localSaveSleepEnhancer.audio_1 == null){
            localSaveSleepEnhancer.audio_1 = completeUrl
            viewModel.getSleepEnhancerData()?.audio_1 = completeUrl
            val data = viewModel.getSleepEnhancerData()
            viewModel.saveSleepEnhancerData(data)
            Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
        }
        else{
            localSaveSleepEnhancer.audio_2 = completeUrl
            viewModel.getSleepEnhancerData()?.audio_2 = completeUrl
            val data = viewModel.getSleepEnhancerData()
            viewModel.saveSleepEnhancerData(data)
            Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
        }

        // save program to the local storage
        if (selectedAlarmProgram == 1) {
            localSaveSleepEnhancer.program_1 = viewModel.getSleepProgramList()?.get(currentPosition)?.thumb.toString()
            program1 = viewModel.getSleepProgramList()?.get(position)?.id.toString()
            Glide.with(requireContext()).load(viewModel.getSleepProgramList()?.get(currentPosition)?.thumb.toString()).into(binding.bottom1)

            Log.e("tag: program1 : ", program1)
        }
        else{
            localSaveSleepEnhancer.program_2 = viewModel.getSleepProgramList()?.get(currentPosition)?.thumb.toString()
            program2 = viewModel.getSleepProgramList()?.get(position)?.id.toString()
            Glide.with(requireContext()).load(viewModel.getSleepProgramList()?.get(currentPosition)?.thumb.toString()).into(binding.bottom2)

            Log.e("tag: program2: ", program2)
        }
    }

    private fun sleepEnhancerDialog(position:Int){

        val data = viewModel.getSleepEnhancerDialogList(viewModel.getSleepProgramList()?.get(position)?.id, 10)
//        Toast.makeText(context, ""+data, Toast.LENGTH_SHORT).show()

        val dialog = Dialog(requireActivity())
       dialog.setContentView(R.layout.sleep_enhancher_dialog)

       dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
       val dialogTitle = dialog.findViewById(R.id.dialog_title) as TextView
       val text10Min = dialog.findViewById(R.id.txt10Min) as TextView
       val text20Min = dialog.findViewById(R.id.txt20Min) as TextView
       val text30Min = dialog.findViewById(R.id.txt30Min) as TextView
       val logo = dialog.findViewById(R.id.logo) as ImageView

        currentPosition = position

        Glide.with(requireContext()).load(viewModel.getSleepProgramList()?.get(position)?.thumb.toString()).into(logo)
        progressBar = dialog.findViewById(R.id.progressBar) as ProgressBar
        recyclerNewSleep = dialog.findViewById(R.id.recyclerNewSleep) as RecyclerView

//        Toast.makeText(context, viewModel.getSleepProgramList()?.get(position)?.id.toString(), Toast.LENGTH_SHORT).show()

        val closeBtnDialog = dialog.findViewById(R.id.closebtndialog) as ImageView
        closeBtnDialog.setOnClickListener {
            if(mediaPlayer.isPlaying){
                mediaPlayer.stop()
                mediaPlayer.reset()
            }

            dialog.dismiss()
        }
       dialogTitle.text = viewModel.getSleepProgramList()?.get(position)?.program_name
       val program = viewModel.getSleepProgramList()?.get(position)?.id
       var duration = 10
       progressBar.setVisible()

       text10Min.setOnClickListener {
           text10Min.setTextColor(Color.WHITE)
           text20Min.setTextColor(Color.GRAY)
           text30Min.setTextColor(Color.GRAY)
           progressBar.setVisible()
           duration = 10
           viewModel.getSleepEnhancerDialogList(program, duration)
       }

       text20Min.setOnClickListener {
           text10Min.setTextColor(Color.GRAY)
           text20Min.setTextColor(Color.WHITE)
           text30Min.setTextColor(Color.GRAY)
           progressBar.setVisible()
           duration = 20

           viewModel.getSleepEnhancerDialogList(program, duration)
       }
       text30Min.setOnClickListener {
           text10Min.setTextColor(Color.GRAY)
           text20Min.setTextColor(Color.GRAY)
           text30Min.setTextColor(Color.WHITE)
           progressBar.setVisible()
           duration = 30
           viewModel.getSleepEnhancerDialogList(program, duration)
       }
        getDurationProgram(duration)

       dialog.show()
   }

    private fun getDurationProgram(duration:Int){
        if (localSaveSleepEnhancer.duration_1 == null) {
            localSaveSleepEnhancer.duration_1 = duration.toString()
            Log.e("tag: DURATION1 : ", program2)
        }
        else if (localSaveSleepEnhancer.duration_1 != null) {
            localSaveSleepEnhancer.duration_2 = duration.toString()
            Log.e("tag: duration2: ", program2)
        }
        else if (localSaveSleepEnhancer.duration_1 != null && localSaveSleepEnhancer.duration_2 != null) {
            localSaveSleepEnhancer.duration_1 = duration.toString()
            Log.e("tag: duration1: ", program2)
        }
        else{
            localSaveSleepEnhancer.duration_1 = duration.toString()
            Log.e("tag: duration2: ", program2)
        }
    }

    private fun sleepEnhancerPlayAudio(position:Int) {

        val customDialog = CustomProgressDialog(requireContext())
        customDialog.showSweetDialog()
        val audioBaseUrl = viewModel.getSleepAudioList()?.base_url_mp3
        val audioSubUrl = viewModel.getSleepAudioList()?.list?.get(position)?.file_name
        val audioUrl = audioBaseUrl+audioSubUrl
        val  uriAudioUrl = Uri.parse(audioUrl)
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
    }

    override fun onResume() {
        super.onResume()
        videoPlay()
    }

    override fun setOnProgramItemClickListener(position: Int, thumb : String?) {
        sleepEnhancerDialog(position)
    }

    // Create a method to make the API request
    fun makeApiRequest() {
        val url = "http://app.dialupdelta.com/web/api/saved_sleep_enhancer"

        val params = HashMap<String, String>()
        params["user_id"] = repository.getAuthData()?.id.toString()

        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                // Handle the response here
                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getBoolean("status")
                val message = jsonResponse.getString("msg")

                if (status) {
                    val result = jsonResponse.getJSONObject("result")
                    val t1 = result.getString("t_1")
                    binding.showAns.text = t1
                    answerForLeft = Integer.parseInt(t1)
                    val t2 = result.getString("t_2")
                    binding.showAns2.text = t2
                    answerForRight = Integer.parseInt(t2)
                    val duration1 = result.getString("duration_1")
                    val duration2 = result.getString("duration_2")
                    val program1 = result.getString("program_1")
                    val program2 = result.getString("program_2")
                    Glide.with(requireContext()).load(program1.toString()).into(binding.bottom1)
                    Glide.with(requireContext()).load(program2.toString()).into(binding.bottom2)

                    val audio1 = result.getString("audio_1")
                    val audio2 = result.getString("audio_2")
                     currentVolume = result.getString("volume").toInt()
                    binding.seekBar111.progress = currentVolume
                    val userId = result.getString("user_id")

                    // Process the retrieved data as needed
                } else {
                    // Handle the case where the status is false
                    Toast.makeText(context, "No data saved", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Handle error cases here
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                return params
            }
        }

        // Add the request to the Volley request queue
        Volley.newRequestQueue(context).add(request)
    }

}
