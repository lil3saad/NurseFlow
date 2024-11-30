package com.example.nurseflowd1.domain.usecases


import com.example.nurseflowd1.datamodels.CardPatient
import com.example.nurseflowd1.room.PatientCardDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomUseCase(val patientcardao : PatientCardDao) {

    suspend fun insertPatientCard(patientcard : CardPatient) {
        CoroutineScope(Dispatchers.IO).launch{
            patientcardao.insertcard(patientcard)
        }
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

    suspend fun readPatientCardList() : List<CardPatient> = withContext(Dispatchers.IO){
        patientcardao.selectallpatientcard()
    }

    suspend fun SearchPatient(usertext : String) = withContext(Dispatchers.IO){
        patientcardao.searchbyname(usertext)
    }

    suspend fun DeletePaitentCards() = withContext(Dispatchers.IO){
        patientcardao.emptyPatientCards()
    }


}