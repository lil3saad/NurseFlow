package com.example.nurseflowd1.datalayer.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nurseflowd1.datalayer.datamodel.CardPatient

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

    @Query("Delete from patientcard")
    suspend fun emptyPatientCards()

    @Query("Select * from patientcard Where  Name Like :searchtext or department like :searchtext or condition like :searchtext or  doctorname like :searchtext")
    suspend fun searchbyname(searchtext : String) : List<CardPatient>

    // Sorting List
    @Query("Select * from patientcard where iscrictal = 1")
    suspend fun getcriticalpatients() : List<CardPatient>

    @Query("Select * from patientcard ORDER BY Name ASC")
    suspend fun SortListByName() : List<CardPatient>

    @Query("Select * from patientcard ORDER BY admissionDate ASC")
    suspend fun SortListByDOA() : List<CardPatient>





}