package com.dialupdelta.ui.sleep_enhancer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dialupdelta.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Handle the actions to be performed when the alarms are triggered
        // For example, you can show notifications, play sounds, etc.
        val url = intent?.getStringExtra("URL_EXTRA")
        val alarmNumber = intent?.getStringExtra("alarmNumber").toString()

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

        }
    }
    private fun showNotification(context: Context?, alarmNumber : String) {
        val channelId = "alarm_channel"
        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(context!!, channelId)
            .setContentTitle("Alarm")
            .setContentText("Wake up ---- "+ alarmNumber)
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
}

