package com.example.nurseflowd1.presentation.screens.shiftreport

sealed class TimePickTypeState {
    object None : TimePickTypeState()
    object StartTime : TimePickTypeState()
    object EndTIme: TimePickTypeState()
    object VitalsRecordTime : TimePickTypeState()
}