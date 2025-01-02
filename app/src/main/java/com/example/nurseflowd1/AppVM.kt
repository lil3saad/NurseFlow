package com.example.nurseflowd1

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Transaction
import com.example.nurseflowd1.datamodels.CardPatient
import com.example.nurseflowd1.datamodels.MedieneInfo
import com.example.nurseflowd1.datamodels.NurseInfo
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.domain.usecases.AWStorageUseCase
import com.example.nurseflowd1.domain.usecases.RoomMediUC
import com.example.nurseflowd1.domain.usecases.RoomPatientUC
import com.example.nurseflowd1.room.RoomPatientListState
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.screens.accountmanage.NurseProfileState
import com.example.nurseflowd1.screens.accountmanage.ProfilePictureState
import com.example.nurseflowd1.screens.nurseauth.NurseDocIdState
import com.example.nurseflowd1.screens.nurseauth.FStorePatientListState
import com.example.nurseflowd1.screens.paitentdash.AddPatientState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.HashMap
import kotlin.Boolean
import kotlin.collections.get

class AppVM(private val navController: NavController,
            private val  storageuc : AWStorageUseCase,
            private val roompatientuc : RoomPatientUC,
            private val roommeduc : RoomMediUC
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
            _authState.value = AuthState.LoginFailed("Please Enter your Credentials")
        } else {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                currentuser = result.user
                Log.d("TAGY", "User Created !VM:CreateUser")
                CreateNurseProfile()
                _authState.value = AuthState.Authenticated
            } catch (e: FirebaseAuthException) {
                Log.w("TAGY", "createUserWithEmail:failure ${e.message}")
                _authState.value = AuthState.SinupFailed("${e.message}")
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
            _authState.value = AuthState.LoginFailed("Login Failed, no Account Found with entered Credentials ")
        }
    }

    fun SingOut() = viewModelScope.launch {
        try {
            auth.signOut()
            currentuser = null
            _authState.value = AuthState.UnAuthenticated
            _NurseDocId.value = NurseDocIdState.NoId
            roompatientuc.DeletePaitentCards()
         // Removes Profile Picture from APP VM
            Log.d("TAGY", "User SignedOut !VM:SingOut")
        } catch (e: FirebaseAuthException) {
            Log.d("TAGY", "FIREBASE LOG ERROR ${e.message}")
        }
    }

    // DATABASE OPERATIONS
    private val firestoredb = Firebase.firestore


    private var _NurseRegisinfo = NurseInfo()
    fun SaveNurseInfoInVm(nurse: NurseInfo) {
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

    private var _firestorePatientList = MutableStateFlow<FStorePatientListState>(FStorePatientListState.emptylist)
    var fstorepatientliststate = _firestorePatientList.asStateFlow()

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
                            Log.d("TAGY", "Document ID : ${document.id}  !VM:GetNurseDocId")
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
   fun FetchP_InfoList(){
        _firestorePatientList.value = FStorePatientListState.Loadinglist
        when (_NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {
                val nursedocid = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                IfPatientsExists(nursedocid) { exists ->
                    if (exists) {
                        Log.d("TAGY", "FETCHING PATIENT DETAILS FROM PATIENT COLLECTION !VM:196")
                        firestoredb.collection("Nurses").document(nursedocid).collection(patientsCollection).get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    try {

                                        val patientList = mutableListOf<CardPatient>()
                                        for (doc in documents) {
                                            val resultp_info = doc.get("patient_info") as HashMap<*, *>

                                            val id = resultp_info["p_patientid"] as String
                                            val name = resultp_info["p_firstname"] as String
                                            val surname = resultp_info["p_surename"] as String
                                            val docname = resultp_info["p_doctor"] as String
                                            val age = resultp_info["p_age"] as Long
                                            val gender = resultp_info["p_gender"] as String
                                            val resultwardno = resultp_info["wardno"] as String
                                            val resultcondition =  resultp_info["condition"] as String
                                            val resultiscritical = resultp_info["iscritical"]as Boolean
                                            val resultdeparment = resultp_info["Department"] as String
                                            val resultDoa = resultp_info["AdmissionDate"] as Long

                                            val patientinfo = CardPatient(
                                                patientid = id,
                                                name = "$name $surname",
                                                doctorname = docname,
                                                age = age.toString(),
                                                gender = gender,
                                                wardno = resultwardno,
                                                condition = resultcondition,
                                                iscrictal = resultiscritical,
                                                Department = resultdeparment,
                                                AdmissionDate = resultDoa
                                            )
                                            patientList.add(patientinfo)
                                        }
                                        Log.d("TAGY", "FETCHED ${patientList.size} Patients from FireBase")
                                        _firestorePatientList.value =   FStorePatientListState.PatientsReceived(patientList)
                                    } catch (e: FirebaseFirestoreException) { Log.d("TAGY", "Error while fetching patientlist ${e.message}") }
                                } else {
                                    _firestorePatientList.value = FStorePatientListState.emptylist
                                    Log.d("TAGY", "No patients found")
                                }
                            }.addOnFailureListener { e ->
                                _firestorePatientList.value = FStorePatientListState.emptylist
                                Log.e("TAGY", "Failed to fetch patients", e)
                            }
                    }
                    else {
                        _firestorePatientList.value = FStorePatientListState.emptylist
                    }
                }
            }
            NurseDocIdState.NoId -> { Log.d("TAGY", "NO NURSEDOC FOUND WITH ANY ID !VM:254") }
            else -> Unit
        }
        // Check If Nurse has added patients collection // just check if the there is patient collection // if there fetch others tell her to add patients
    }
    fun IfPatientsExists(nursedocid: String, callback: (Boolean) -> Unit) {
        Log.d("TAGY", "Patients Check Called")
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

    fun SavePatientInfoFirestore(pinfo: PatientInfo) = viewModelScope.launch {
        Log.d("TAGY" , "Adding Patient To Room")
        val roompatientcard = CardPatient(
            patientid = pinfo.p_patientid ,
            name = "${pinfo.p_firstname} ${pinfo.p_surename}",
            condition = pinfo.condition,
            doctorname = pinfo.p_doctor,
            gender = pinfo.p_gender,
            age = pinfo.p_age.toString(),
            wardno = pinfo.wardno,
            iscrictal = pinfo.iscritical ,
            Department = pinfo.Department,
            AdmissionDate = pinfo.AdmissionDate
            )

        _Addpatientstate.value = AddPatientState.AddingPatient
        when (_NurseDocId.value) {
            is NurseDocIdState.CurrentNurseId -> {

                val nursedocid = (_NurseDocId.value as NurseDocIdState.CurrentNurseId).string
                val NurseDocRef = firestoredb.collection("Nurses").document(nursedocid)

                val NursePatients = "n_patients"
                val p_medicines = "medicines"

                val patientdoc = hashMapOf(
                    "patient_info" to pinfo
                )

                suspend fun PatientExists(patientid: String, callback: (Boolean) -> Unit) =
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
                    }
                    else {
                        val PatientDocRef = NurseDocRef.collection(NursePatients).document(pinfo.p_patientid) // Doc with Patient_Id Gets Created
                        // Store patient info as an object
                        PatientDocRef.set(patientdoc)
                            .addOnCompleteListener { document ->
                                Log.d("TAGY", "New Patient Doc Added with DocId: ${PatientDocRef.id} !VM:343")
                                // Creating subcollections for the patient
                                PatientDocRef.collection(p_medicines).add("test" to 1)
                                CoroutineScope(Dispatchers.IO).launch{
                                    savePatientCardInRoom(roompatientcard)
                                }
                                _Addpatientstate.value = AddPatientState.PatientAdded
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
    fun AddPatientResset() {
        _Addpatientstate.value = AddPatientState.idle
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
    // ROOM OPERATIONS
    private var _CardPatientList : MutableStateFlow<RoomPatientListState> = MutableStateFlow(RoomPatientListState.idle)
    var cardpatientlist : StateFlow<RoomPatientListState> = _CardPatientList.asStateFlow()

    fun savePatientCardInRoom(patientCardEntity: CardPatient) = viewModelScope.launch {
        roompatientuc.insertPatientCard(patientCardEntity)
        _CardPatientList.value = RoomPatientListState.NewAdded
    }

    fun getCardPatietnList() = viewModelScope.launch {
        Log.d("TAGY", "GET PATIENTS FUNCTION CALLED !VM:491")
        try {
            _CardPatientList.value = RoomPatientListState.loading
            val roomlist = roompatientuc.readPatientCardList()
            Log.d("TAGY", "GOT LIST FROM ROOM ${roomlist.size} !VM : 522")
              if( roomlist.isEmpty()) { FetchP_InfoList()
                _firestorePatientList.collect { state ->
                        when (state) {
                            FStorePatientListState.Loadinglist -> { _CardPatientList.value = RoomPatientListState.loading }
                            is FStorePatientListState.PatientsReceived -> {
                                Log.d("TAGY", "CALLED AFTER PATIENTS ${state.patientlist.size} RECEVIED FROM FIRESTORE !VM : 503")
                                // Save all patients concurrently and wait for completion
                                roompatientuc.insertPatientList(state.patientlist)
                                val getroomlist = roompatientuc.readPatientCardList()
                                Log.d("TAGY", "GOT LIST FROM ROOM ${getroomlist.size} !VM : 535")
                                _CardPatientList.value = RoomPatientListState.FullReadList(getroomlist)
                            }
                            FStorePatientListState.emptylist -> {
                                _CardPatientList.value = RoomPatientListState.emptylist
                            }
                        }
                }
            } else{
                Log.d("TAGY", "LIST NOT EMPTY IN ROOM  ${roomlist.size} !VM : 550")
                _CardPatientList.value = RoomPatientListState.FullReadList(roomlist)
            }
        }catch (e: Exception) {
            Log.e("TAGY", "Error fetching patient list", e)
            _CardPatientList.value = RoomPatientListState.Error(e.message ?: "Unknown error occurred")
        }
    }
    //fetch search list from room
    fun getSearchResult(usertext : String) = viewModelScope.launch{
        val roompatienlist = roompatientuc.SearchPatient(usertext)
        if(roompatienlist.isEmpty()){
            _CardPatientList.value = RoomPatientListState.Error("No Patients Found")
        }else _CardPatientList.value = RoomPatientListState.SearchList(roompatienlist)
    }
    //fetch critical list from room
    fun getCriticalList() = viewModelScope.launch{
        val roompatienlist = roompatientuc.getCriticalist()
        if(roompatienlist.isEmpty()){
            Log.d("TAGY" , "NO PATIENTS FOUND WITH THAT TEST !VM:getSearchResult,551")
        }else _CardPatientList.value = RoomPatientListState.CriticalList(roompatienlist)
    }


    // MEDICINE FUNCTIONS
     fun insertmedi(medieneInfo: MedieneInfo) = viewModelScope.launch{
        roommeduc.insertPatientCard(medieneInfo)
    }
    fun deletemedi(medieneInfo: MedieneInfo) = viewModelScope.launch{
        roommeduc.deletePatientCard(medieneInfo)
    }
    fun updatemedi(medieneInfo: MedieneInfo) = viewModelScope.launch{
        roommeduc.updatePatientCard(medieneInfo)
    }

    private var _PatientMediList :  MutableStateFlow<List<MedieneInfo>> = MutableStateFlow(emptyList())
    var patientmedilist : StateFlow<List<MedieneInfo>> = _PatientMediList


   fun patientmedilist(patientid: String )  = viewModelScope.launch{
         _PatientMediList.value = emptyList<MedieneInfo>()
        _PatientMediList.value =   roommeduc.fetchPatientMedi(patientid)
    }


    // OTHERS
    private  var _AppBarTitleState : MutableStateFlow<AppBarTitleState> = MutableStateFlow(AppBarTitleState.NoTopAppBar)
    val appbartitlestate : StateFlow<AppBarTitleState> = _AppBarTitleState.asStateFlow()


    private  var _AppBarNavIconState : MutableStateFlow<NavigationIconState> = MutableStateFlow(NavigationIconState.None)
    val appbariconstate : StateFlow<NavigationIconState>  = _AppBarNavIconState.asStateFlow()

    private  var _AppBarColorsState : MutableStateFlow<AppBarColorState> = MutableStateFlow(
        AppBarColorState.DefaultColors)
    val appbarcolorstate : StateFlow<AppBarColorState> = _AppBarColorsState.asStateFlow()

    fun ChangeTopBarState(barstate : AppBarTitleState, colorState: AppBarColorState, iconState: NavigationIconState  ) = viewModelScope.launch{
        _AppBarTitleState.value = barstate
        _AppBarNavIconState.value = iconState
        _AppBarColorsState.value = colorState
    }

    private  var _bottombarstate : MutableStateFlow<BottomBarState> = MutableStateFlow(
        BottomBarState.NurseDashBoard)
    val bottombarstate : StateFlow<BottomBarState> = _bottombarstate.asStateFlow()

    fun ChangeBottomBarState(barstate : BottomBarState) = viewModelScope.launch{
        _bottombarstate.value = barstate
    }

}
sealed class AuthState {
    object Idle : AuthState()
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object LoadingAuth : AuthState()
    data class LoginFailed(val message: String) : AuthState()
    data class SinupFailed(val message : String) : AuthState()
}