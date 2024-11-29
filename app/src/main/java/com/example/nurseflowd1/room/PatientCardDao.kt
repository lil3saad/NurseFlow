package com.example.nurseflowd1.room

import androidx.compose.runtime.MutableState
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao()
interface PatientCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE ) // what does this do
    fun insertcard(patientcard : PatientCardEntity) : Long

    @Update
    fun updatecard(patientcard : PatientCardEntity)

    @Delete
    fun deletecard(patentcard : PatientCardEntity)

    @Query("Select * from patientcards")
    fun selectallpatientcard() : Flow<List<PatientCardEntity>>

    @Query("Select * from patientcards where Name like :username")
    fun searchbyname(username : String) : Flow<List<PatientCardEntity>>

}