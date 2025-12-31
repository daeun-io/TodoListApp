package com.gmail.devleedaeun.todolistapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gmail.devleedaeun.todolistapp.ui.main.MainActivity
import okhttp3.internal.notify

class AlarmReceiver(): BroadcastReceiver() {

    private val channelID = "ALARM_CHANNEL_ID"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null){
            // Notification channel registration
            val channel = NotificationChannel(
                channelID,
                "Alarm Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Define an intent which operates when an alarm is clicked
            // (without it AutoCancel not occurs)
            val activityIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, intent?.getIntExtra("id",0)?:0, activityIntent, PendingIntent.FLAG_IMMUTABLE
            )

            Log.d("id/alarm", intent?.getIntExtra("id",0).toString())

            // Build an alarm
            val notification = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.baseline_check_circle_24)
                .setContentTitle(intent?.getStringExtra("title")?:"My Todo")
                .setContentIntent(pendingIntent) // Connect action when clicked
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            // Launch the alarm
            notificationManager.notify(intent?.getIntExtra("id", 0)?:0, notification)
        }
    }
}