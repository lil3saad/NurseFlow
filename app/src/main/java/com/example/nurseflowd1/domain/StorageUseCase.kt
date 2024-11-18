package com.example.nurseflowd1.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StorageUseCase() {
    val _profilestate  : MutableStateFlow<ProfilePictureState> = MutableStateFlow(ProfilePictureState.Empty)
}