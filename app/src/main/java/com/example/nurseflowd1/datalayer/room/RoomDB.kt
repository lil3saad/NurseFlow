package com.example.nurseflowd1.datalayer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nurseflowd1.datalayer.datamodel.CardPatient
import com.example.nurseflowd1.datalayer.datamodel.MedieneInfo
import com.example.nurseflowd1.datalayer.datamodel.NurseNote

@Database( entities = [CardPatient::class, MedieneInfo::class , NurseNote::class] , version = 1 , exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun getpatientcardDAO() : PatientCardDao

    abstract fun getmedicinedDAO() : MedicineDao

    abstract fun getnoteDAO() : NurseNoteDao

    companion object {
        @kotlin.jvm.Volatile
        private var instance : RoomDB? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) : RoomDB = instance ?: synchronized(LOCK){
             instance ?:  Room.databaseBuilder(context = context , RoomDB::class.java , "PatientData").build()
        }
    }


}