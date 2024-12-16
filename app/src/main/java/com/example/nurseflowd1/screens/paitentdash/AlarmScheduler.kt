package com.example.nurseflowd1.screens.paitentdash

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent

import android.content.Intent
import com.example.nurseflowd1.datamodels.MedieneInfo
import android.util.Log
import java.util.Calendar
import kotlin.random.Random

class AlarmScheduler(val context : android.content.Context) {

    @SuppressLint("MissingPermission") // Set the XML
    fun SchedulerAlarm(medicine : MedieneInfo){
        // 1. Get the time and the medicine name and the patient name to trigger the notification or alarm for
        // handle notification first
        // then set alarm
        // 2. Handle merge conflicts
        val alrammanager : AlarmManager = context.getSystemService(android.content.Context.ALARM_SERVICE) as android.app.AlarmManager

        val notiintent = Intent( context , MedsNotiReceiver::class.java).also { intent ->
            intent.putExtra("MedName" , medicine.medi_name)
            intent.putExtra("MedType" , medicine.medi_type)
            intent.putExtra("PName" , "DummyPatientName") // Also Store the Patient Name with each medicine
        } // send the intent to the notif Manager

        val medihour : Int = medicine.meditime.substring(startIndex = 0 , endIndex = 2).toInt()
        val medimins : Int = medicine.meditime.substring(3, medicine.meditime.length).toInt()
        Log.d("FUCKME" , " hour = $medihour , mins = $medimins ")
        val alarmtime = ConvertTimetoMillis(medihour,medimins)
        val broadcastrequestcode  = Random.nextInt()
        Log.d("FUCKME" , " PendingIntent Request Id $broadcastrequestcode ")


        alrammanager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP , // WHAT DOES WAKE UP REALLY MEAN 
            alarmtime,
            PendingIntent.getBroadcast(context,
             broadcastrequestcode , // Request Code
                notiintent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
  private fun ConvertTimetoMillis(hour : Int , min : Int) : Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis


    }
}