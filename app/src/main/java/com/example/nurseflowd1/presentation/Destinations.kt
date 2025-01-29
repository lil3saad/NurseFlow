package com.example.nurseflowd1.presentation

sealed class Destinations(val ref : String) {

    object LoginScreen : Destinations("loginscreen")
    object AuthScreen : Destinations("authscreen")
    object RegisScreen : Destinations("regisscreen")

    //Account Settings
    object AccountScreen : Destinations("accountscreen")
    object UpdateProfileScreen : Destinations("updatescreen")
    object AccSettingsScreen: Destinations("settingscreen")

    object NurseDboardScreen : Destinations("nursedb")
    object PatientRegisterScreen : Destinations("patientinfo")

    // Patient DashBoard
    object PatientDashboardScreen : Destinations("patientdash/{patientid}/{patientname}")
    object AddMediceneScreen : Destinations("addmedi/{patientid}/{patientname}")


    object ShiftReportScreen : Destinations("ShiftReport")
    object AddReportScreen: Destinations("WriteReport")

    object NurseNotes : Destinations("NurseNotes")
    object AddNoteScreen {
        const val route =  "add_note_screen/{UpdateNote}/{NoteId}/{notetitle}/{notebody}"
        fun createRoute(
            isUpdate: Boolean,
            noteId: Long,
            noteTitle: String,
            noteBody: String
        ) = "add_note_screen/$isUpdate/$noteId/$noteTitle/$noteBody"
    }


}