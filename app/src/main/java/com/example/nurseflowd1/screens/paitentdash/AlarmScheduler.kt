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

    val alrammanager : AlarmManager = context.getSystemService(android.content.Context.ALARM_SERVICE) as android.app.AlarmManager

    @SuppressLint("MissingPermission") // Set the XML
    fun ScheduleAlarmNotification(medicine : MedieneInfo) {

        val medihour : Int = medicine.meditime.substring(startIndex = 0 , endIndex = 2).toInt()
        val medimins : Int = medicine.meditime.substring(3, medicine.meditime.length).toInt()
        Log.d("FUCKME" , " hour = $medihour , mins = $medimins ")


        val triggertime = ConvertTimetoMillis(medihour,medimins)
        val endtime = medicine.endmeditime


        val broadcastrequestcode  = Random.nextInt()
        val notiintent = Intent( context , MedsNotiReceiver::class.java).also { intent ->
            intent.putExtra("BroadcastKey" , broadcastrequestcode)
            intent.putExtra("Endtime" , endtime)
            intent.putExtra("MedName" , medicine.medi_name)
            intent.putExtra("MedType" , medicine.medi_type)
            intent.putExtra("PName" , medicine.patientname) // Also Store the Patient Name with each medicine
        } // send the intent to the notif Manager

        val broadcastalarm = PendingIntent.getBroadcast(
            context,
            broadcastrequestcode,
            notiintent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        SetExactRpeating(triggertime , endtime , broadcastalarm)
    }

    fun SetExactRpeating(Triggertime : Long, endtime : Long, pendingintent : PendingIntent) {
        if(Triggertime >= endtime){
            Log.d("FUCKME" , "Cancelled Alarm ")
            alrammanager.cancel(pendingintent)
        }
        else {
            Log.d("FUCKME" , "Should Launch Alarm with Tigtime : $Triggertime && endtime : $endtime")
            alrammanager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP , // WHAT DOES WAKE UP REALLY MEAN
                Triggertime,
                pendingintent
            )
        }
    }

  private fun ConvertTimetoMillis(hour : Int , min : Int) : Long {
        val triggertime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
      if ( triggertime.timeInMillis <= System.currentTimeMillis()) {
          Log.d("FUCKME" , "MADARCHOD") // If Trigger Time is lesser  then Current time then Push it to  a day , trigger time passed is already over
          triggertime.add(Calendar.DAY_OF_YEAR, 1);
      }
        return triggertime.timeInMillis


  }
}
