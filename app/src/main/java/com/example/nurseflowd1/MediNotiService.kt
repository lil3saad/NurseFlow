package com.example.nurseflowd1

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.transition.Visibility
import androidx.core.app.NotificationCompat

class MediNotiService(
    private val context: android.content.Context
){
    companion object {
        const val channelid = "medchannel"
    }
    // Mark as Done
    fun showNotifcation(patientname : String , msg : String) : NotificationCompat.Builder {
        val activityintent = Intent(context, MainActivity::class.java) // This Intenet Just Launches a Brand New NurseDashBoard and the one which is already opened


        val pendingIntent = PendingIntent.getActivity(context , 1 , activityintent ,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                PendingIntent.FLAG_IMMUTABLE
            }else 0
            ) // What are Flags in Android

        return NotificationCompat.Builder(context , channelid)

            .setSmallIcon(R.drawable.syringe) //
            .setContentTitle(patientname)
            .setContentText("upcomming medicene at $msg")
            .setContentIntent(pendingIntent) // This is the Intent which is sent when the users clicks on the notification
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE).setPublicVersion(
                NotificationCompat.Builder(context , channelid).setContentTitle("HiddenGandu").setContentText("TERRI MA KI CHUT").build()
            )


        // Public - full content
        // Secret - Not shown up in Lock Screen
        // Private - shows in lock Screen but with not content
    }
}

//.setStyle(
//                NotificationCompat.BigTextStyle // in a email notification only small text is displayed first if you drop down more content of the email and it is displayed in big  // Explore more Notification Style in Documentation
//            )