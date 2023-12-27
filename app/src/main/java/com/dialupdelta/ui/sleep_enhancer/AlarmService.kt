import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.dialupdelta.R
import com.dialupdelta.ui.sleep_enhancer.AlarmReceiver

class AlarmService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle the alarm logic here

        // You can start the AlarmReceiver code here if needed
        val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java)
        // Put extras in the intent if necessary
        // alarmIntent.putExtra("URL_EXTRA", ...)
        // alarmIntent.putExtra("alarmNumber", ...)

        // Send the broadcast to trigger the alarm
        applicationContext.sendBroadcast(alarmIntent)

        // Return START_STICKY to ensure the service restarts if it's killed
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val notification = createForegroundNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createForegroundNotification(): Notification {
        // Create and return a notification for your foreground service
        val channelId = "foreground_service_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Foreground Service")
            .setContentText("Running alarms...")
            .setSmallIcon(R.drawable.logo_toolbar)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return notificationBuilder.build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}
