package com.example.nurseflowd1.presentation.screens.paitentdash

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.nurseflowd1.R
import androidx.core.app.NotificationCompat
import com.example.nurseflowd1.NotificationReferences
import kotlin.random.Random

class MedsNotiReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, passedintent : Intent) {
        Log.d("MYNOTIES" , "BROADCAST RECEIVED BUT AND NOTIFICATION LAUNCHED")
        val notiManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        val broadcastcode = passedintent.getIntExtra("BroadcastKey" , 1)
        val patientname = passedintent.getStringExtra("PName")
        val mediname = passedintent.getStringExtra("MedName")
        val meditype = passedintent.getStringExtra("MedType")


        val medinotification = NotificationCompat.Builder(context , NotificationReferences.MediChannelId.ref)
            .setSmallIcon(R.drawable.syringe)
            .setContentTitle(patientname) // Patient Name
            .setContentText("Time for  $mediname $meditype") // Time for
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        //NotificationID // Make it the Dosage + MedId
        notiManager.notify(Random.nextInt(),medinotification)


        val myalarmscheduler = AlarmScheduler(context)
        val nextTriggerTime = System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES // From the Exact Currenttime or the start time of the broadcast , Change this to INTERVAL DAY
        val endtime = passedintent.getLongExtra("Endtime", 10000)
        myalarmscheduler.SetExactRpeating(nextTriggerTime , endtime,
            PendingIntent.getBroadcast(
                context,
                broadcastcode,
                passedintent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

    }
}