package com.example.nurseflowd1.screens.paitentdash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.nurseflowd1.R
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class MedsNotiReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, passedintent : Intent) {
        val notiManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager


        val patientname = passedintent.getStringExtra("PName")
        val mediname = passedintent.getStringExtra("MedName")
        val meditype = passedintent.getStringExtra("MedType")

        val medinotification = NotificationCompat.Builder(context , "notichannel101")
            .setSmallIcon(R.drawable.syringe)
            .setContentTitle("NurseFlow") // Patient Name
            .setContentText("Time to provide $mediname $meditype to Mr/Ms $patientname") // Time for
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        //NotificationID // Make it the Dosage + MedId
        notiManager.notify(Random.nextInt(),medinotification)
    }
}