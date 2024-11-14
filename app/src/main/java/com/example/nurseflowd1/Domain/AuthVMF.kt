package com.example.nurseflowd1.Domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

class AuthVMF(val navController: NavController) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppVM::class.java)){
            return AppVM(navController) as T
        }
        throw IllegalArgumentException("ViewModel not found ")
    }
}