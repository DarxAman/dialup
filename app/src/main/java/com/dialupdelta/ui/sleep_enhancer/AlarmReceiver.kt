package com.dialupdelta.ui.sleep_enhancer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dialupdelta.R
import com.dialupdelta.ui.get_to_sleep.RingedAlarm
import com.google.gson.Gson
import org.json.JSONObject
import java.io.Serializable

class AlarmReceiver() : BroadcastReceiver(), Serializable {

    private val localSaveSleepEnhancer = LocalSaveSleepEnhancer()
    private var userId = ""
    var globalT1: String = ""
    var globalT2: String = ""
    var globalDuration1: String = ""
    var globalDuration2: String = ""
    var globalProgram1: String = ""
    var globalProgram2: String = ""
    var globalAudio1: String = ""
    var globalAudio2: String = ""
    var globalVolume: String = ""
    private var alarmListener: RingedAlarm? = null

    fun setAlarmListener(listener: RingedAlarm) {
        this.alarmListener = listener
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        // Handle the actions to be performed when the alarms are triggered
        // For example, you can show notifications, play sounds, etc.
        val url = intent?.getStringExtra("URL_EXTRA")
        val alarmNumber = intent?.getStringExtra("alarmNumber").toString()
        userId = intent?.getStringExtra("userId").toString()

        Log.e("userID", userId)

        val fragmentManagerContext = CommonObject()
        val fragmentManager = CommonObject.fragmentManagerContext

//       = gson.fromJson(intent?.getStringExtra("fragmentManager")) as FragmentManager

        if (alarmNumber == "1" || alarmNumber == "2") {

            if (url != null) {
                val mediaPlayer = MediaPlayer()

                try {
                    mediaPlayer.setDataSource(url)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                showNotification(context, alarmNumber)
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }

                getSleepSavedData(context, alarmNumber)

                if(alarmNumber == "1"){
                    localSaveSleepEnhancer.time1 = "0"
                    localSaveSleepEnhancer.program_1 = ""
                    localSaveSleepEnhancer.audio_1 = ""
                    alarmListener?.playedAlarm("1")
                }else if(alarmNumber == "2"){
                    localSaveSleepEnhancer.time1 = "0"
                    localSaveSleepEnhancer.program_1 = ""
                    localSaveSleepEnhancer.audio_1 = ""
                    alarmListener?.playedAlarm("2")
                }

            }
        }else{
            if (url != null) {
                val videoDialog = VideoDialogFragment.newInstance(url, alarmNumber)
                alarmListener?.playedAlarm("3")
//                val fragmentManager = (context as? Activity)?.fragmentManager
                if (fragmentManager != null) {
//                    videoDialog.isCancelable = false
                    videoDialog. setStyle(DialogFragment.STYLE_NORMAL,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//                    videoDialog.
                    videoDialog.show(fragmentManager, "videoDialog")
                    showNotification(context, alarmNumber)
                }else if(context is FragmentActivity){
//                    Toast.makeText(context, "yes", Toast.LENGTH_LONG).show()
                }else{
//                    Toast.makeText(context, "NO", Toast.LENGTH_LONG).show()
                    showNotification(context, alarmNumber)

                }
            }
        }
    }

    private fun showNotification(context: Context?, alarmNumber: String) {
        val channelId = "alarm_channel"
        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(context!!, channelId)
            .setContentTitle("Alarm")
            .setContentText("Wake up ---- " + alarmNumber)
            .setSmallIcon(R.drawable.logo_toolbar)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alarm Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun setSleepData(context: Context?, alarmNumber: String, response: String) {
        val url = "https://app.dialupdelta.com/web/api/sleep_enhancer_saver"

        var jsonObject = JSONObject(response)


        // Form data parameters
        val params: MutableMap<String, String> = HashMap()
        if(alarmNumber.equals("1")){
            params["t_1"] = "45"
            params["t_2"] = jsonObject.getJSONObject("result").getString("t_2")
            params["duration_2"] = jsonObject.getJSONObject("result").getString("duration_2")
            params["duration_1"] = ""
            params["program_1"] = ""
            params["program_2"] = jsonObject.getJSONObject("result").getString("program_2")
            params["audio_1"] = ""
            params["audio_2"] = jsonObject.getJSONObject("result").getString("audio_2")
            params["volume"] = jsonObject.getJSONObject("result").getString("volume")
            params["user_id"] = userId
        }else if(alarmNumber.equals("2")){
            params["t_1"] = jsonObject.getJSONObject("result").getString("t_1")
            params["t_2"] = "135"
            params["duration_1"] = jsonObject.getJSONObject("result").getString("duration_1")
            params["duration_2"] = ""
            params["program_1"] = jsonObject.getJSONObject("result").getString("program_1")
            params["program_2"] = ""
            params["audio_1"] = jsonObject.getJSONObject("result").getString("audio_1")
            params["audio_2"] = ""
            params["volume"] = jsonObject.getJSONObject("result").getString("volume")
            params["user_id"] = userId
        }

        Log.e("params : ", ""+params)

        // Create a POST request using Volley
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                // Handle response from the server
                // You can parse the response if needed
                println("Response data saved : $response")
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

    fun getSleepSavedData(context: Context?, alarmNumber: String) {
        val url = "https://app.dialupdelta.com/web/api/saved_sleep_enhancer"

        // Form data parameters
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = userId

        // Create a POST request using Volley
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                // Handle response from the server
                // You can parse the response if needed
                println("Response data get: $response")

                val gson = Gson()
                val sleepEnhancerData = gson.fromJson(response, SleepEnhancerData::class.java)

                globalT1 = sleepEnhancerData.t_1
                globalT2 = sleepEnhancerData.t_2
                globalDuration1 = sleepEnhancerData.duration_1
                globalDuration2 = sleepEnhancerData.duration_2
                globalProgram1 = sleepEnhancerData.program_1
                globalProgram2 = sleepEnhancerData.program_2
                globalAudio1 = sleepEnhancerData.audio_1
                globalAudio2 = sleepEnhancerData.audio_2
                globalVolume = sleepEnhancerData.volume

                setSleepData(context, alarmNumber, response)

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
}


