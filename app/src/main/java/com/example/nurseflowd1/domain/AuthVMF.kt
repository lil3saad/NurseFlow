package com.example.nurseflowd1.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.nurseflowd1.domain.usecases.AWStorageUseCase
import com.example.nurseflowd1.domain.usecases.RoomMediUC
import com.example.nurseflowd1.domain.usecases.NurseNoteUC
import com.example.nurseflowd1.domain.usecases.RoomPatientUC

//class AuthVMF(val navController: NavController , val storageuc : AWStorageUseCase , val roomuc : RoomPatientUC, val roomMediUC: RoomMediUC , val roomNoteUc : NurseNoteUC) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(AppVM::class.java)) {
//            return AppVM(navController , storageuc , roomuc, roomMediUC , roomNoteUc) as T
//        }
//        throw IllegalArgumentException("ViewModel not found ")
//    }
//}