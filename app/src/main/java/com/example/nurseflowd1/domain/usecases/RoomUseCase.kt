package com.example.nurseflowd1.domain.usecases


import com.example.nurseflowd1.room.PatientCardDao
import com.example.nurseflowd1.room.PatientCardEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomUseCase(val patientcardao : PatientCardDao) {

    suspend fun insertPatientCard(patientcard : PatientCardEntity) {
        CoroutineScope(Dispatchers.IO).launch{
            patientcardao.insertcard(patientcard)
        }
    }


}