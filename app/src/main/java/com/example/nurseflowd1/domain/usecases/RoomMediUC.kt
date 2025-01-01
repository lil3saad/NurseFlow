package com.example.nurseflowd1.domain.usecases

import com.example.nurseflowd1.datamodels.CardPatient
import com.example.nurseflowd1.datamodels.MedieneInfo
import com.example.nurseflowd1.room.MedicineDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomMediUC(private val medidao : MedicineDao){
    suspend fun insertPatientCard(medi_info : MedieneInfo) {
        CoroutineScope(Dispatchers.IO).launch{
           medidao.InsertMedi(medi_info)
        }
    }
    suspend fun updatePatientCard(medi_info : MedieneInfo) {
        CoroutineScope(Dispatchers.IO).launch{
            medidao.IpdateMedi(medi_info)
        }
    }
    suspend fun deletePatientCard(medi_info : MedieneInfo) {
        CoroutineScope(Dispatchers.IO).launch{
           medidao.DeleteMedi(medi_info)
        }
    }
    suspend fun fetchPatientMedi(patientid : String) : List<MedieneInfo> =
        withContext(Dispatchers.IO) {
            medidao.GetPatientMediList(patientid)
        }
}