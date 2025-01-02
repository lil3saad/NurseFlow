package com.example.nurseflowd1.screens.paitentdash.medication

sealed class MediTypeState(val ref : String) {
    object idle : MediTypeState("Idle")
    object tablet : MediTypeState("Tablet")
    object Tonic : MediTypeState("Tonic")
    object Capsule : MediTypeState("Capsule")
    object Drops : MediTypeState("Drops")
    object Injection : MediTypeState("Injection")
    object Others : MediTypeState("Others")
    object Empty : MediTypeState("Empty")
}