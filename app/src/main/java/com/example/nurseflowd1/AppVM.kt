package com.example.nurseflowd1

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.nurseflowd1.datamodels.NurseInfo
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.domain.StorageUseCase
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.TopAppBarState
import com.example.nurseflowd1.screens.accountmanage.NurseProfileState
import com.example.nurseflowd1.screens.accountmanage.ProfilePictureState
import com.example.nurseflowd1.screens.nurseauth.NurseDocIdState
import com.example.nurseflowd1.screens.nurseauth.PatientListState
import com.example.nurseflowd1.screens.patientboarding.AddPatientState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.HashMap
import kotlin.collections.get

class AppVM( private val navController: NavController,
  private val  storageuc : StorageUseCase
) : ViewModel() {

    // USER AUTHENTICATION
    val auth: FirebaseAuth = Firebase.auth
    var currentuser: FirebaseUser? = auth.currentUser

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Idle)
    val authstate: StateFlow<AuthState> = _authState.asStateFlow()

    fun authStatus() = viewModelScope.launch {
        if ( currentuser != null) _authState.value = AuthState.Authenticated
        else
        {
            Log.d("TAGY" , "AUTH STATUS : UNAuthenticated ")
            _authState.value = AuthState.UnAuthenticated

        }
    }

    fun CreateUser(email: String, password: String) = viewModelScope.launch {
        _authState.value = AuthState.LoadingAuth
        delay(15) // to  obeserve can react to the change state
        if (email.isEmpty() && password.isEmpty()) {
            _authState.value = AuthState.Failed("Please Enter your Credentials")
        } else {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                currentuser = result.user
                Log.d("TAGY", "User Created !VM:CreateUser")
                CreateNurseProfile()
                _authState.value = AuthState.Authenticated
            } catch (e: FirebaseAuthException) {
                Log.w("TAGY", "createUserWithEmail:failure ${e.message}")
                _authState.value = AuthState.Failed("${e.message}")
            }
        }
    }

    fun LoginUser(email: String, password: String) = viewModelScope.launch {
        Log.d("TAGY", "LoginFunctionCalled !VM:LoginUser")
        _authState.value = AuthState.LoadingAuth
        // Lets say the user is on the state failed once , if he fails again the state says same even tho you switch it , you are switching value is emiting but before the UI can react the state is changing back to same value and there is no creation is not changed there will be no new chaange obbserved so everytime
        // make a reinti state object\
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            currentuser = auth.currentUser
            _authState.value = AuthState.Authenticated
            Log.d("TAGY", "User Logged In !VM:LoginUser")
        } catch (e: FirebaseAuthException) {
            Log.w("TAGY", "createUserWithEmail:failure ${e.message}")
            _authState.value = AuthState.Failed("Login Failed, no Account Found with entered Credentials ")
        }
    }

    fun SingOut() = viewModelScope.launch {
        try {
            auth.signOut()
            currentuser = null
            _authState.value = AuthState.UnAuthenticated
            _NurseDocId.value = NurseDocIdState.NoId
         // Removes Profile Picture from APP VM
            Log.d("TAGY", "User SignedOut !VM:SingOut")
        } catch (e: FirebaseAuthException) {
            Log.d("TAGY", "FIREBASE LOG ERROR ${e.message}")
        }
    }

    // DATABASE OPERATIONS
    private val firestoredb = Firebase.firestore


    private var _NurseRegisinfo = NurseInfo()
    fun SaveNurseInfo(nurse: NurseInfo) {
        _NurseRegisinfo.N_age = nurse.N_age
        _NurseRegisinfo.N_gender = nurse.N_gender
        _NurseRegisinfo.N_name = nurse.N_name
        _NurseRegisinfo.N_surname = nurse.N_surname
        _NurseRegisinfo.N_hospitalid = nurse.N_hospitalid
        _NurseRegisinfo.N_hospitalname = nurse.N_hospitalname
        _NurseRegisinfo.N_council = nurse.N_council
        _NurseRegisinfo.N_registrationid = nurse.N_registrationid
        _NurseRegisinfo.uid = nurse.uid
        _NurseRegisinfo.profilepicid = nurse.profilepicid
        Log.d("TAGY", "Nurse Profile Saved In with ProfileId = ${nurse.profilepicid} !VM:SaveNurseInfo")
    }

    fun CreateNurseProfile() = viewModelScope.launch {
        if (auth.currentUser != null) {
            _NurseRegisinfo.uid = auth.currentUser!!.uid
            firestoredb.collection("Nurses").add(_NurseRegisinfo)
                .addOnCompleteListener { document ->
                    Log.d(
                        "TAGY",
                        "Nurse Doc Added with DocId : ${document.result.id} !VM:CreateNurseProfile"
                    )

                }.addOnFailureListener { e ->
                    Log.d(
                        "TAGY",
                        "NurseDoc Could not be created : ${e.message} !VM:CreateNurseProfile"
                    )
                }
        }
    }

    private var _NurseDocId: MutableStateFlow<NurseDocIdState> = MutableStateFlow(NurseDocIdState.idle)
    var NurseDocId: StateFlow<NurseDocIdState> = _NurseDocId

    private var _patientinfolist = MutableStateFlow<PatientListState>(PatientListState.emptylist)
    val paitientinfolist: StateFlow<PatientListState> = _patientinfolist

    fun GetNurseDocId() {
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
                        Log.d("TAGY", "No documents found for UID: MOTHER FUCKER $uid")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("TAGY", "Error getting documents: ", exception)
                }
        } else {
            Log.d("TAGY", "NO NURSE AUTHENTICATED YET")
        }
    }

    val patientsCollection = "n_patients"
    //Fetch the PatientInfo Object  Field  from FireStore // Function should be called only after receiving the NurseDocID
    fun FetchP_InfoList() {
        Log.d("TAGY" , "FETCH FUNCTION BEING CALLED")
        _patientinfolist.value = PatientListState.Loadinglist
        when (_NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                IfPatientsExists(nursedocid) { exists ->
                    if (exists) {
                        Log.d("TAGY", "FETCHING PATIENT DETAILS FROM PATIENT COLLECTION")
                        firestoredb.collection("Nurses").document(nursedocid)
                            .collection(patientsCollection).get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    try {
                                        val patientList = mutableListOf<PatientInfo>()
                                        for (doc in documents) {
                                            Log.d("TAGY", "Fetched Patient Full Doc :  $doc")

                                            val result = doc.get("patient_info") as HashMap<*, *>
                                            val name = result["p_name"] as String
                                            val surname = result["p_surename"] as String
                                            val age = result["p_age"] as Long
                                            val docname = result["p_doctor"] as String
                                            val gender = result["p_gender"] as String
                                            val patientid = result["p_patientid"] as String
                                            val phoneno = result["p_phoneno"] as String

                                            val receivedpinfo = PatientInfo(
                                                p_name = name,
                                                p_surename = surname,
                                                p_age = age.toInt(),
                                                p_doctor = docname,
                                                p_gender = gender,
                                                p_patientid = patientid,
                                                p_phoneno = phoneno
                                            )
                                            patientList.add(receivedpinfo)
                                        }
                                        _patientinfolist.value =
                                            PatientListState.PatientsReceived(patientList) // Update the StateFlow at Once
                                    } catch (e: FirebaseFirestoreException) {
                                        Log.d(
                                            "TAGY",
                                            "Error while fetching patientlist ${e.message}"
                                        )
                                    }

                                } else {
                                    _patientinfolist.value = PatientListState.emptylist
                                    Log.d("TAGY", "No patients found")
                                }
                            }.addOnFailureListener { e ->
                                _patientinfolist.value = PatientListState.emptylist
                                Log.e("TAGY", "Failed to fetch patients", e)
                            }
                    } else {
                        _patientinfolist.value = PatientListState.emptylist
                    }
                }
            }
            NurseDocIdState.NoId -> {
                Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:SavePatientInfoFireStore")
            }
            else -> Unit
        }
        // Check If Nurse has added patients collection // just check if the there is patient collection // if there fetch others tell her to add patients

    }
    fun IfPatientsExists(nursedocid: String, callback: (Boolean) -> Unit) {
        Log.d("TAGY", "Patients Check Called ")
        firestoredb.collection("Nurses").document(nursedocid).collection(patientsCollection)
            .limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Collection does not exist (or is empty)
                    callback(false)
                    Log.d(
                        "TAGY",
                        "The collection 'n_patients' does not exists. !VM:IfPatientsExists"
                    )
                } else {
                    // Collection exists (has at least one document)
                    Log.d("TAGY", "The collection 'n_patients' exists. ${querySnapshot.documents}")
                    callback(true)
                }
            }.addOnFailureListener { e ->
                Log.w("TAGY", "Error checking collection existence", e)
                callback(false)
            }
    }

    private var _Addpatientstate = MutableStateFlow<AddPatientState>(AddPatientState.idle)
    var addPatientState: StateFlow<AddPatientState> = _Addpatientstate

    //Save PatientInfo + _Pvitals and put in the Patients Collection with there own doc
    fun SavePatientInfoFirestore(pinfo: PatientInfo) = viewModelScope.launch {
        _Addpatientstate.value = AddPatientState.AddingPatient
        when (_NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {

                val nursedocid = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                val NurseDocRef = firestoredb.collection("Nurses").document(nursedocid)

                val NursePatients = "n_patients"
                val p_medicines = "medicines"

                val patientdoc = hashMapOf("patient_info" to pinfo ,
                        "IsCritical" to false
                )

                fun PatientExists(patientid: String, callback: (Boolean) -> Unit) =
                    viewModelScope.launch {
                        NurseDocRef.collection(NursePatients).document(patientid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    callback(true)
                                } else {
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
                        _Addpatientstate.value =
                            AddPatientState.AddPatientFailed("Patient with this ID already exists , Can't Create New Paitent with same Id ")
                        Log.d(
                            "TAGY",
                            "Patient with this ID already exists , Can't Create New Paitent with same Id "
                        )
                    } else {
                        val PatientDocRef = NurseDocRef.collection(NursePatients)
                            .document(pinfo.p_patientid) // Doc with Patient_Id Gets Created
                        // Store patient info as an object
                        PatientDocRef.set(patientdoc)
                            .addOnCompleteListener { document ->
                                Log.d(
                                    "TAGY",
                                    "New Patient Doc Added with DocId: ${PatientDocRef.id}"
                                )
                                // Creating subcollections for the patient
                                PatientDocRef.collection(p_medicines).add("test" to 1)
                                _Addpatientstate.value = AddPatientState.idle
                                navController.popBackStack(
                                    route = Destinations.NurseDboardScreen.ref,
                                    inclusive = false
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.d("TAGY", "NurseDoc could not be created: ${e.message}")
                            }
                    }
                }
            }

            NurseDocIdState.NoId -> {
                Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:SavePatientInfoFireStore")
            }
            else -> Unit
        }
    }

    //NurseProfile Operations
    private var _NurseProfileState: MutableStateFlow<NurseProfileState> = MutableStateFlow(NurseProfileState.Loading)
    val nurseprofilestate: MutableStateFlow<NurseProfileState> = _NurseProfileState
    fun FetchNurseProfile() = viewModelScope.launch {
        when (_NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                try {
                    val fbnursedoc = firestoredb.collection("Nurses").document(nursedocid).get().await()
                    if (fbnursedoc.exists()) {
                        val nurseInfo = fbnursedoc.toObject(NurseInfo::class.java)
                        if (nurseInfo != null) {
                            Log.d("TAGY", "Nurse Info: $nurseInfo !VM:FetchNurseProfile")
                            _NurseProfileState.value = NurseProfileState.Fetched(nurseInfo)
                        } else {
                            Log.d("TAGY", "No NurseInfo object found !VM:FetchNurseProfile")
                        }
                    } else {
                        Log.d(
                            "TAGY",
                            "No document found with ID: $nursedocid !VM:FetchNurseProfile"
                        )
                    }
                } catch (e: FirebaseFirestoreException) {
                    Log.d("TAGY", "${e.message}  !VM:FetchNurseProfile")
                }
            }
            NurseDocIdState.NoId -> {
                Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:FetchNurseProfile")
            }
            else -> Unit
        }
    }

    fun UpdateNurseProfile() = viewModelScope.launch {
        _NurseProfileState.value = NurseProfileState.Loading
        when (_NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                try {
                    Log.d("TAGY", "Updated  NurseDoc with ${_NurseRegisinfo.profilepicid }!VM:UpdateNurseProfile")
                    firestoredb.collection("Nurses").document(nursedocid).set(_NurseRegisinfo).await()
                    _NurseProfileState.value = NurseProfileState.UpdateDone
                    navController.popBackStack()
                    Log.d("TAGY", "the Document should be Updated !VM:UpdateNurseProfile")

                } catch (e: FirebaseFirestoreException) {
                    _NurseProfileState.value = NurseProfileState.Failed(e.message!!)
                    Log.d("TAGY", "${e.message}  !VM:UpdateNurseProfile")
                }
            }
            NurseDocIdState.NoId -> {
                Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:UpdateNurseProfile")
            }
            else -> Unit
        }
    }

    private var _profilepicstate : MutableStateFlow<ProfilePictureState> = MutableStateFlow(ProfilePictureState.empty)
    val profilepiucstate : StateFlow<ProfilePictureState> = _profilepicstate

    // Accounts Page Operations
    fun saveProfileUri(uri: Uri, context: Context) = viewModelScope.launch {
        _profilepicstate.value = ProfilePictureState.Loading
        when (val state = _NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid = state.string // Send the path to save profiepic id
                val path = firestoredb.collection("Nurses").document(nursedocid)
                storageuc.postProfilePicture(uri, context , path , getProfilePicID() ).collect{ profilestate ->
                    _profilepicstate.value = profilestate
                }
            }
            NurseDocIdState.NoId -> {
                Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:FetchNurseProfile")
            }
            else -> Unit
        }
    }

    fun getProfilePicState() = viewModelScope.launch {
        _profilepicstate.value = ProfilePictureState.Loading
        val ProfilePicId = getProfilePicID() // Fetch Id From FireStore
        if(ProfilePicId == "default"){
            Log.d("TAGY" , "User profilepicid = default")
            _profilepicstate.value = ProfilePictureState.default
        }
        else {
            storageuc.getProfilePicture(ProfilePicId).collect { state ->
                _profilepicstate.value = state
            }
        }
    }

    suspend fun getProfilePicID() : String {
        var profilepicid = ""
        when (val state = _NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid = state.string // Send the path to save profiepic id
                val doc = firestoredb.collection("Nurses").document(nursedocid).get().await()
                profilepicid  = doc.get("profilepicid") as String
            }
            NurseDocIdState.NoId -> {
                Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:getProfilePicID")
            }
            else -> Unit
        }

        return  profilepicid
    }

    fun DeleteProfilePicState() = viewModelScope.launch {
        _profilepicstate.value = ProfilePictureState.Loading
        when (val state = _NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid = state.string // Send the path to save profiepic id
                val path = firestoredb.collection("Nurses").document(nursedocid)
                val fileid = getProfilePicID()
                storageuc.DeleteProfile(fileid,path).collect{ state ->
                    _profilepicstate.value = state
                }
            }
            NurseDocIdState.NoId -> {
                Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:FetchNurseProfile")
            }
            else -> Unit
        }
    }


    // OTHERS
    private  var _topappbarstate : MutableStateFlow<TopAppBarState> = MutableStateFlow(TopAppBarState.AppNameBar)
    val topappbarstate : StateFlow<TopAppBarState> = _topappbarstate.asStateFlow()

    fun SetTopBarState(barstate : TopAppBarState) = viewModelScope.launch{
        _topappbarstate.value = barstate
    }

    private  var _bottombarstate : MutableStateFlow<BottomBarState> = MutableStateFlow(
        BottomBarState.NurseDashBoard)
    val bottombarstate : StateFlow<BottomBarState> = _bottombarstate.asStateFlow()

    fun SetBottomBarState(barstate : BottomBarState) = viewModelScope.launch{
        _bottombarstate.value = barstate
    }


}
sealed class AuthState {
    object Idle : AuthState()
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object LoadingAuth : AuthState()
    data class Failed(val message: String) : AuthState()
}