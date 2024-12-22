package com.example.nurseflowd1.room

import androidx.compose.runtime.MutableState
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nurseflowd1.datamodels.CardPatient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao()
interface PatientCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE ) // what does this do
    suspend fun insertcard(patientcard : CardPatient) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPatients(patientList: List<CardPatient>)

    @Update
    fun updatecard(patientcard : CardPatient)

    @Delete
    fun deletecard(patentcard : CardPatient)

    @Query("Select * from patientcard")
    fun selectallpatientcard() : List<CardPatient>

    @Query("Select * from patientcard Where  Name Like :username")
    suspend fun searchbyname(username : String) : List<CardPatient>

    @Query("Select * from patientcard where iscrictal = 1")
    suspend fun getcriticalpatients() : List<CardPatient>

    @Query("Delete from patientcard")
    suspend fun emptyPatientCards()



}