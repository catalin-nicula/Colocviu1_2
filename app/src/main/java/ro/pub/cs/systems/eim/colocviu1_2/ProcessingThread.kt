package ro.pub.cs.systems.eim.colocviu1_2

import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log
import ro.pub.cs.systems.eim.colocviu1_2.Constants.BROADCAST_RECEIVER_EXTRA
import ro.pub.cs.systems.eim.colocviu1_2.Constants.PROCESSING_THREAD_TAG
import ro.pub.cs.systems.eim.colocviu1_2.Constants.actionType
import java.util.Date
import java.util.Random
import kotlin.math.sqrt

class ProcessingThread(private val context: Context, sumAll: Int) : Thread() {
    private var isRunning = true

    private val random = Random()
    private val sumAllO = sumAll

    override fun run() {
        Log.d(
            PROCESSING_THREAD_TAG,
            "Thread has started! PID: " + Process.myPid() + " TID: " + Process.myTid()
        )
        while (isRunning) {
            sendMessage()
            sleep()
        }
        Log.d(PROCESSING_THREAD_TAG, "Thread has stopped!")
    }

    private fun sendMessage() {
        val intent = Intent()
        intent.setAction(actionType)
        intent.putExtra(
            BROADCAST_RECEIVER_EXTRA,
            Date(System.currentTimeMillis()).toString() + " " + sumAllO.toString()
        )
        context.sendBroadcast(intent)
    }

    private fun sleep() {
        try {
            sleep(2000)
        } catch (interruptedException: InterruptedException) {
            interruptedException.printStackTrace()
        }
    }

    fun stopThread() {
        isRunning = false
    }
}