package com.example.nurseflowd1.domain.usecases


import android.util.Log
import com.example.nurseflowd1.datamodels.CardPatient
import com.example.nurseflowd1.room.PatientCardDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomPatientUC(val patientcardao : PatientCardDao) {

    suspend fun insertPatientCard(patientcard : CardPatient) {
            patientcardao.insertcard(patientcard)
    }
    suspend fun insertPatientList(patientlist : List<CardPatient>){
        patientcardao.insertAllPatients(patientlist)
    }
    suspend fun updatePatientCard(patientcard : CardPatient) {
        CoroutineScope(Dispatchers.IO).launch{
            patientcardao.updatecard(patientcard)
        }
    }
    suspend fun deletePatientCard(patientcard : CardPatient) {
        CoroutineScope(Dispatchers.IO).launch{
            patientcardao.deletecard(patientcard)
        }
    }
    suspend fun DeletePaitentCards() = withContext(Dispatchers.IO){
        patientcardao.emptyPatientCards()
    }


    // List Operations
    suspend fun readPatientCardList() : List<CardPatient> = withContext(Dispatchers.IO){
        Log.d("TAGY" , "Fetching Patients from Room !RoomPatientUC:31")
        patientcardao.selectallpatientcard()
    }

    suspend fun SearchPatient(usertext : String) = withContext(Dispatchers.IO){
        patientcardao.searchbyname(usertext)
    }

    suspend fun getCriticalist() = withContext(Dispatchers.IO){
        patientcardao.getcriticalpatients()
    }
    suspend fun SortListByName() = withContext(Dispatchers.IO){
        patientcardao.SortListByName()
    }
    suspend fun SortListByDOA() = withContext(Dispatchers.IO){
        patientcardao.SortListByDOA()
    }

}