package com.example.nurseflowd1.domain


sealed class ProfilePictureState{
    object Empty : ProfilePictureState()
    data class Picture(val uri : android.net.Uri) : ProfilePictureState()
}