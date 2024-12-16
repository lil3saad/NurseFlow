package com.example.nurseflowd1.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nurseflowd1.datamodels.MedieneInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertMedi(medi : MedieneInfo) : Long

    @Update
    fun IpdateMedi(medi : MedieneInfo)

    @Delete
    fun DeleteMedi(medi : MedieneInfo)

    @Query("SELECT patientid, medid, * FROM medstable WHERE patientid LIKE :patientid GROUP BY patientid, medid")
    fun GetPatientMediList(patientid : String) : List<MedieneInfo>

}