package com.example.nurseflowd1.screens.shiftreport

sealed class TimePickTypeState {
    object None : TimePickTypeState()
    object StartTime : TimePickTypeState()
    object EndTIme: TimePickTypeState()
    object VitalsRecordTime : TimePickTypeState()
}