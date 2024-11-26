package com.example.nurseflowd1.screens.accountmanage

import android.graphics.Bitmap


sealed class ProfilePictureState{
    object empty : ProfilePictureState()
    object default : ProfilePictureState()
    object Added : ProfilePictureState()
    data class Fetched(val image : Bitmap) : ProfilePictureState()
    object Loading : ProfilePictureState()
    data class failed(val msg : String) : ProfilePictureState()

}