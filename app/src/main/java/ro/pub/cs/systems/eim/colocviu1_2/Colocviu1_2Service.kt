package ro.pub.cs.systems.eim.colocviu1_2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import ro.pub.cs.systems.eim.colocviu1_2.Constants.SUM_RESULT
import java.util.Objects

class Colocviu1_2Service : Service() {
    lateinit var processingThread: ProcessingThread
    override fun onCreate() {
        super.onCreate()

        val CHANNEL_ID = "my_channel_01"
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        (Objects.requireNonNull(getSystemService(NOTIFICATION_SERVICE)) as NotificationManager).createNotificationChannel(
            channel
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("")
            .setContentText("").build()

        startForeground(1, notification)

    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(Constants.tag, "onStartCommand() method was invoked with the following parameters: " + startId)
        val sumAll = intent.getIntExtra(SUM_RESULT, -1)
        processingThread = ProcessingThread(this, sumAll)
        processingThread.start()
        Log.d("Colocviu1_2Service", "onStartCommand() method was invoked with the following parameters: " + sumAll)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        processingThread.stopThread()
    }
}