package com.example.nurseflowd1

import android.app.Application
import android.os.Build
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CreateMediceneChannel()
    }
   fun CreateMediceneChannel(){
       // The Android SDK Version of the user must be Above Oreo
       if(Build.VERSION.SDK_INT  >=  Build.VERSION_CODES.O) {
            val medichannel = NotificationChannel(
                MediNotiService.channelid,
                "Medication Reminders", // Will be displayed in App Settings
                NotificationManager.IMPORTANCE_HIGH          // Define the level of importance of the notifications , there are level of importance  of notification , Surf Through all the Levels Of Importance
            )
           medichannel.description = "Get Patient Medication Reminders"

           val notimanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           notimanager.createNotificationChannel(medichannel) // Channel Created
       }
       // set channel description
   }
}