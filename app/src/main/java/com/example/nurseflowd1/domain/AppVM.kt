package com.example.nurseflowd1.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nurseflowd1.datamodels.NurseRegisInfo
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.screens.nurseauth.NurseDocIdState
import com.example.nurseflowd1.screens.nurseauth.PatientListState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.HashMap

class AppVM : ViewModel() {
    // USER AUTHENTICATION
    val auth: FirebaseAuth = Firebase.auth
    var currentuser : FirebaseUser? = auth.currentUser

    private val _authState : MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Idle)
    val authstate :  StateFlow<AuthState> =  _authState

    fun authStatus() = viewModelScope.launch {
        if( currentuser != null) _authState.value = AuthState.Authenticated
        else _authState.value = AuthState.UnAuthenticated
    }

    fun CreateUser(email: String, password: String)  = viewModelScope.launch {
        _authState.value = AuthState.LoadingAuth
        delay(15) // so obeserve can react to the change state
        if(email.isEmpty() && password.isEmpty()){
            _authState.value = AuthState.Failed("Please Enter your Credentials")
        }else {
            try{
                val result = auth.createUserWithEmailAndPassword(email,password).await() // auth.createuser since auth.createuser takes some time and by default coroutines are asyncronous the auth.createuser process runs on some seperate coroutine theread and variable current user gets intialized in another coroutine by the current auth user , it does not wait for the auth.createuser to create and switch to the new user , because it is asynchronous and does not wait for process to completee , to make the coroutine aware of prior processs to finish and pause the all other processs in the coroutine until the first process is done we use await()
                currentuser = result.user
                Log.d("TAGY" , "User Created !VM:CreateUser")
                _authState.value = AuthState.Authenticated
            }catch (e : FirebaseAuthException){
                Log.w("TAGY", "createUserWithEmail:failure ${e.message}")
                _authState.value = AuthState.Failed("${e.message}")
            }
        }
    }
    fun LoginUser(email: String , password: String) = viewModelScope.launch {
        Log.d("TAGY" , "LoginFunctionCalled !VM:LoginUser")
        _authState.value = AuthState.LoadingAuth
        // Lets say the user is on the state failed once , if he fails again the state says same even tho you switch it , you are switching value is emiting but before the UI can react the state is changing back to same value and there is no creation is not changed there will be no new chaange obbserved so everytime
        // make a reinti state object\
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                currentuser = auth.currentUser
                _authState.value = AuthState.Authenticated
                Log.d("TAGY" , "User Logged In !VM:LoginUser")
            }catch(e : FirebaseAuthException){
                Log.w("TAGY", "createUserWithEmail:failure ${e.message}")
                _authState.value = AuthState.Failed("Login Failed, no Account Found with entered Credentials ")
            }
    }

    fun SingOut() = viewModelScope.launch {
        try {
            auth.signOut()
            currentuser =  null
            _NurseDocId.value = NurseDocIdState.NoId
            _patientinfolist.value = PatientListState.idlelist
                _authState.value = AuthState.UnAuthenticated
            Log.d("TAGY" , "User SignedOut !VM:SingOut")
        }catch (e : FirebaseAuthException) {
            Log.d("TAGY" , "FIREBASE LOG ERROR ${e.message}")
        }
    }

    // DATABASE OPERATIONS
    private val firestoredb = Firebase.firestore


    private var _NurseRegisinfo = NurseRegisInfo()
    fun SaveNurseInfo(nurse : NurseRegisInfo){
        _NurseRegisinfo.N_age = nurse.N_age
        _NurseRegisinfo.N_gender = nurse.N_gender
        _NurseRegisinfo.N_name = nurse.N_name
        _NurseRegisinfo.N_surname = nurse.N_surname
        _NurseRegisinfo.N_hospitalid = nurse.N_hospitalid
        _NurseRegisinfo.N_hospitalname = nurse.N_hospitalname
        _NurseRegisinfo.N_license_id = nurse.N_license_id
        Log.d("TAGY" , "Nurse Profile Saved In !VM:SaveNurseInfo")
    }
    fun CreateNurseProfile() = viewModelScope.launch{
        if(auth.currentUser != null) {
            _NurseRegisinfo.uid = auth.currentUser!!.uid
            firestoredb.collection("Nurses").add(_NurseRegisinfo)
                .addOnCompleteListener{ document ->
                    Log.d("TAGY" , "Nurse Doc Added with DocId : ${document.result.id} !VM:CreateNurseProfile")

                }.addOnFailureListener{ e ->
                    Log.d("TAGY" , "NurseDoc Could not be created : ${e.message} !VM:CreateNurseProfile")
                }
        }
    }


    // DataBase References
    private var _NurseDocId : MutableStateFlow<NurseDocIdState> = MutableStateFlow(NurseDocIdState.NoId)
    var NurseDocId : StateFlow<NurseDocIdState> = _NurseDocId
    fun GetNurseDocId(){
        val uid = currentuser?.uid
        if (uid != null) {
            firestoredb.collection("Nurses")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        for (document in querySnapshot.documents) {
                            // Process each document
                            Log.d("TAGY", "Document ID : ${document.id} => ${document.data} !VM:GetNurseDocId")
                           _NurseDocId.value = NurseDocIdState.CurrentNurseId(document.id)
                        }
                    } else {
                        Log.d("TAGY", "No documents found for UID: $uid")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("TAGY", "Error getting documents: ", exception)
                }
        } else {
            Log.d("TAGY", "NO NURSE AUTHENTICATED YET")
        }
    }

    //Save PatientInfo + _Pvitals and put in the Patients Collection with there own doc
    fun SavePatientInfoFirestore( pinfo : PatientInfo) = viewModelScope.launch {
        when(_NurseDocId.value){
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid  = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                val NurseDocRef = firestoredb.collection("Nurses").document(nursedocid)

                val NursePatients = "n_patients"
                val p_medicines = "medicines"
                val p_notes = "notes"
                val p_reports = "p_reports"
                val patientdoc = hashMapOf("patient_info" to pinfo)

                fun PatientExists(patientid: String, callback : (Boolean) -> Unit) = viewModelScope.launch {
                    NurseDocRef.collection(NursePatients).document(patientid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                Log.d("TAGY", "DOCUMENT IS ALREADY EXISTING WITH DOC ID: $patientid")
                                callback(true)
                            } else {
                                Log.d("TAGY", "NO DOCUMENT EXISTING, CREATING DOC: $patientid")
                                callback(false)
                            }
                        }
                        .addOnFailureListener {
                            Log.d("TAGY", "Error checking document existence: ${it.message}")
                            callback(false) // Treat failure as non-existent
                        }
                }

                PatientExists(pinfo.p_patientid) { exists ->
                    if (exists) {
                        Log.d("TAGY", "Patient with this ID already exists.Can't Create New Paitent")
                    } else {
                        val PatientDocRef = NurseDocRef.collection(NursePatients).document(pinfo.p_patientid) // Doc with Patient_Id Gets Created
                        // Store patient info as an object
                        PatientDocRef.set(patientdoc)
                            .addOnCompleteListener {
                                    document ->
                                Log.d("TAGY", "New Patient Doc Added with DocId: ${PatientDocRef.id}")
                                // Creating subcollections for the patient
                                PatientDocRef.collection(p_medicines).add( "test" to 1)
                                PatientDocRef.collection(p_notes).add( "test" to 1)
                                PatientDocRef.collection(p_reports).add( "test" to 1)
                            }
                            .addOnFailureListener { e ->
                                Log.d("TAGY", "NurseDoc could not be created: ${e.message}")
                            }
                    }
                }
            }
            NurseDocIdState.NoId -> {
                Log.d("TAGY" , "NO NURSEDOC FOUND WITH ANY ID !VM:SavePatientInfoFireStore")
            }
        }
    }



    private var _patientinfolist  = MutableStateFlow<PatientListState>(PatientListState.idlelist)
    val paitientinfolist : StateFlow<PatientListState> = _patientinfolist
    val patientsCollection = "n_patients"
    //Fetch the PatientInfo Object  Field  from FireStore // Function should be called only after receiving the NurseDocID
    fun FetchP_InfoList(){
        when(_NurseDocId.value){
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid  = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                IfPatientsExists(nursedocid) { exists ->
                    if(exists){
                        Log.d("TAGY" , "FETCHING PATIENT DETAILS FROM PATIENT COLLECTION")
                        firestoredb.collection("Nurses").document(nursedocid).collection(patientsCollection)
                            .get()
                            .addOnSuccessListener {
                                    documents -> if (!documents.isEmpty) {
                                try {
                                    val patientList = mutableListOf<PatientInfo>()
                                    for (doc in documents) {
                                        Log.d("TAGY", "Fetched Patient Full Doc :  $doc")

                                        val result  = doc.get("patient_info") as HashMap<*, *>
                                        val name = result["p_name"] as String
                                        val surname= result["p_surename"] as String
                                        val age  = result["p_age"] as String
                                        val docname  = result["p_doctor"] as String
                                        val gender = result["p_gender"] as String
                                        val patientid = result["p_patientid"] as String
                                        val  phoneno = result["p_phoneno"] as String

                                        val receivedpinfo = PatientInfo(
                                            p_name = name,
                                            p_surename = surname,
                                            p_age = age,
                                            p_doctor = docname,
                                            p_gender = gender,
                                            p_patientid = patientid,
                                            p_phoneno = phoneno
                                        )
                                        patientList.add(receivedpinfo)
                                    }
                                    _patientinfolist.value = PatientListState.PatientsReceived(patientList) // Update the StateFlow at Once
                                }catch (e : FirebaseFirestoreException){
                                    Log.d("TAGY", "Error while fetching patientlist ${e.message}" )
                                }

                            }else {
                                Log.d("TAGY", "No patients found")
                            }
                            }
                            .addOnFailureListener { e ->
                                Log.e("TAGY", "Failed to fetch patients", e)
                            }
                    }
                }
            }

            NurseDocIdState.NoId -> {
                Log.d("TAGY" , "NO NURSEDOC FOUND WITH ANY ID !VM:SavePatientInfoFireStore")
            }
        }
        // Check If Nurse has added patients collection // just check if the there is patient collection // if there fetch others tell her to add patients

    }
    fun IfPatientsExists( nursedocid : String , callback : (Boolean) -> Unit)  {
        Log.d("TAGY", "Patients Check Called ")
        firestoredb.collection("Nurses").document( nursedocid ).collection(patientsCollection).limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Collection does not exist (or is empty)
                    callback(false)
                    Log.d("TAGY", "The collection 'n_patients' does not exists. !VM:IfPatientsExists")
                } else {
                    // Collection exists (has at least one document)
                    Log.d("TAGY", "WHAT HELLE The collection 'n_patients' exists. ${querySnapshot.documents}")
                    callback(true)
                }
            }.addOnFailureListener { e ->
                Log.w("TAGY", "Error checking collection existence", e)
                callback(false)
            }
    }

}
sealed class AuthState {
        object Idle : AuthState()
        object Authenticated : AuthState()
        object UnAuthenticated : AuthState()
        object LoadingAuth : AuthState()
        data class Failed(val message: String) : AuthState()
}
