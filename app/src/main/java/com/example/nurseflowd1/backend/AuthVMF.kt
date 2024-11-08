package com.example.nurseflowd1.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthVMF : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppVM::class.java)){
            return AppVM() as T
        }
        throw IllegalArgumentException("ViewModel not found ")
    }
}