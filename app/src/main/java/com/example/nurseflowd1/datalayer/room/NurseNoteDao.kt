package com.example.nurseflowd1.datalayer.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nurseflowd1.datalayer.datamodel.NurseNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NurseNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun CreateNote(note : NurseNote) : Long // why Long ?

    @Update
    suspend fun UpdateNote(note: NurseNote)

    @Delete
    suspend fun DeleteNote(note: NurseNote)

    @Query("Select * from nursenotes")
    fun ReadallNotes() : Flow<List<NurseNote>>

    @Query("Select * from nursenotes where title like :usertext")
    fun SearchNote(usertext : String) : Flow<List<NurseNote>>
}