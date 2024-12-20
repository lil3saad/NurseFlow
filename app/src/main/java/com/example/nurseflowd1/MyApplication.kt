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
        val MediChannel = NotificationChannel( NotificationReferences.MediChannelId.ref , "Medication Reminder"  ,
            NotificationManager.IMPORTANCE_HIGH   ).apply {
                description = "Get Patient Reminders"
        }

        val notiManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notiManager.createNotificationChannel(MediChannel)
    }


}

sealed class NotificationReferences(val ref : String){
    object MediChannelId : NotificationReferences(ref = "notichannel101")
}