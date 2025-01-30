package com.example.nurseflowd1

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.nurseflowd1.koin.appwriteModule
import com.example.nurseflowd1.koin.roomModule
import com.example.nurseflowd1.koin.usecasesModule
import com.example.nurseflowd1.koin.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CreateNotiChannel()

        startKoin {
            androidContext(this@MyApplication)
            modules( listOf ( roomModule , usecasesModule , viewmodelModule , appwriteModule) )
        }
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