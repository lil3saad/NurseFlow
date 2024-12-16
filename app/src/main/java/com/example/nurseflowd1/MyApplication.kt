package com.example.nurseflowd1

import android.app.Application
import android.os.Build
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CreateNotiChannel()
    }

    private fun CreateNotiChannel() {
        val MediChannel = NotificationChannel( "notichannel101" , "Medication Reminder"  ,
            NotificationManager.IMPORTANCE_HIGH   ).apply {
                description = "Get Patient Reminders"
        } // What Do Importance levels do , i think default has no pop up and High has pop up

        val notiManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notiManager.createNotificationChannel(MediChannel)
    }


}