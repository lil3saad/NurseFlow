package com.example.nurseflowd1.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.domain.usecases.AWStorageUseCase
import com.example.nurseflowd1.domain.usecases.RoomUseCase

class AuthVMF(val navController: NavController , val storageuc : AWStorageUseCase , val roomuc : RoomUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppVM::class.java)) {
            return AppVM(navController , storageuc , roomuc) as T
        }
        throw IllegalArgumentException("ViewModel not found ")
    }
}