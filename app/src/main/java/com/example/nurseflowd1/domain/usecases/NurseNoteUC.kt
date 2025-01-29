package com.example.nurseflowd1.domain.usecases


import android.util.Log
import androidx.room.Room
import com.example.nurseflowd1.datalayer.datamodel.NurseNote
import com.example.nurseflowd1.datalayer.room.NurseNoteDao
import com.example.nurseflowd1.presentation.screens.nursenotes.RoomNoteListState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NurseNoteUC(val notedao : NurseNoteDao) {

    val firestore = Firebase.firestore
    suspend fun createNote(note : NurseNote , nursedocid : String) {
        notedao.CreateNote(note)
        firestore.collection("Nurses").document(nursedocid).collection("NurseNotes")
            .document("${note.noteid}").set(note)
            .addOnSuccessListener{
                Log.d("NOTEUPDATE" , "Failed While Adding Note !NurseNoteUC:24")
            }
            .addOnFailureListener{ ex ->
                Log.d("NOTEUPDATE" , "Failed While Adding Note ${ex.message!!} !NurseNoteUC:27")
            }
    }

    suspend fun deleteNote(note: NurseNote, nursedocid: String) {

        notedao.DeleteNote(note)
        firestore.collection("Nurses").document(nursedocid).collection("NurseNotes")
            .document("${note.noteid}").delete()
            .addOnCompleteListener{ task ->
                Log.d("NOTEUPDATE", "NOTE DELETE ${task.isComplete}")
            }
            .addOnFailureListener{ ex ->
                Log.d("NOTEUPDATE", "NOTE COULD NOT BE DELETED ${ex.message}")
            }

    }

    suspend fun updateNote(note: NurseNote, nursedocid: String) {
        notedao.UpdateNote(note)
        firestore.collection("Nurses").document(nursedocid).collection("NurseNotes")
            .document("${note.noteid}").set(note)
            .addOnCompleteListener{ task ->
                Log.d("NOTEUPDATE", "NOTE UPDATE ${task.isComplete}")
            }
            .addOnFailureListener{ ex ->
                Log.d("NOTEUPDATE", "NOTE COULD NOT BE UPDATED  ${ex.message}")
            }
    }


    fun getAllNotes(nursedocid : String) : Flow<RoomNoteListState>  = flow {
        try {
            notedao.ReadallNotes()
                .onStart {
                    emit(RoomNoteListState.Loading)
                }
                .catch { exception ->
                    emit(RoomNoteListState.ListError(exception.message!!)) }
                .collect{ list ->
                    if(list.isEmpty())
                    { // Fetch Notes From FireBase
                        val ans : RoomNoteListState =  getNotesFirestore(nursedocid)
                        when(val state = ans) {
                            RoomNoteListState.EmptyList -> { emit(RoomNoteListState.EmptyList ) }
                            is RoomNoteListState.ListError -> {      emit(RoomNoteListState.ListError(state.error)) }
                            is RoomNoteListState.Receive -> {
                                val firebaselist = state.list
                                for (note in firebaselist){
                                    createNote(note , nursedocid)
                                }
                                Log.d("NOTEUPDATE" , "STORED FIREBASE NOTES IN ROOM AND EMIITED FLOW")
                                emit(RoomNoteListState.Receive(firebaselist) )
                            }
                           else -> Unit
                        }
                    }
                    else emit(RoomNoteListState.Receive(list) )
                }
        } catch (e : Exception) { Log.d("TAGY" , "${e.message}" )}

     }


    suspend fun getNotesFirestore(nursedocid: String): RoomNoteListState = suspendCoroutine { continuation ->
        Log.d("NOTEUPDATE", "CHECK FIRESTORE FOR NOTES")
        firestore.collection("Nurses").document(nursedocid).collection("NurseNotes").get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.d("NOTEUPDATE", "FIRESTORE HAS NO NOTES FOR THIS NURSE")
                    continuation.resume(RoomNoteListState.EmptyList)
                } else {
                    Log.d("NOTEUPDATE", "NURSE NOTES FOUND")
                    val anslist = result.documents.map { doc ->
                        NurseNote(
                            noteid = doc.getLong("noteid") ?: 0L,
                            title = doc.getString("title") ?: "",
                            body = doc.getString("body") ?: ""
                        ).also {
                            Log.d("NOTEUPDATE", "FETCHED $doc from FireStore")
                        }
                    }
                    continuation.resume(RoomNoteListState.Receive(anslist))
                }
            }
            .addOnFailureListener { exception ->
                Log.d("NOTEUPDATE", "FireStore error ${exception.message}")
                continuation.resume(RoomNoteListState.ListError(exception.message ?: "Unknown error"))
            }
    }

    fun getNotesFirestoreMine(nursedocid : String) : RoomNoteListState {
        // FireBase Performance Asynchronous Operations Compared to the Android Thread , Which is Why
//        ChatGPT said:
//        ChatGPT
//        The issue with your getNotesFirestore function is that Firestore queries run asynchronously, but your function is returning ans immediately, before Firestore has a chance to fetch the data. By the time Firestore's success or failure listeners execute, the function has already returned RoomNoteListState.EmptyList.
//
//        Why is this happening?
//        Firestore queries run asynchronously:
//        The function calls firestore.collection(...).get() which starts an asynchronous request.
//        The function does not wait for Firestore to complete fetching.
//        Instead, it proceeds to return ans, which is still RoomNoteListState.EmptyList at that point.
//        Listeners (addOnSuccessListener, addOnFailureListener) execute later:
//        When Firestore finally gets the data, the success/failure listeners update ans, but it's already too late because the function has returned.

        Log.d("NOTEUPDATE" , "CHECK FIRESTORE FOR NOTES ")
        var ans : RoomNoteListState = RoomNoteListState.EmptyList

        firestore.collection("Nurses").document(nursedocid).collection("NurseNotes").get()
            .addOnSuccessListener{ result ->
                if(result.isEmpty){
                    Log.d("NOTEUPDATE" , " FIRESTORE HAS NO NOTES FOR THIS NURSE ")
                    ans =  RoomNoteListState.EmptyList
                }else{
                    Log.d("NOTEUPDATE" , "NURSE NOTES FOUNDS")
                    val docs = result.documents
                    val anslist = mutableListOf<NurseNote>()
                    for( doc in docs){
                        val note = NurseNote( noteid = doc.get("noteid") as Long , title = doc.get("title") as String, body = doc.get("body") as String)
                        Log.d("NOTEUPDATE" , "FETCHED $doc from FireStore")
                        anslist.add(note)
                    }
                    ans = RoomNoteListState.Receive(anslist)
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("NOTEUPDATE" , "FireStore error ${exception.message}")
                ans = RoomNoteListState.ListError(exception.message!!)
            }

        return ans
    }

     fun searcNote(usertext  : String) : Flow<RoomNoteListState> = flow {
         try {
             notedao.SearchNote(usertext)
                 .onStart { emit(RoomNoteListState.Loading) }
                 .catch { expection ->
                     emit(RoomNoteListState.ListError(expection.message!!))
                 }
                 .collect{ list ->
                     if(list.isEmpty())
                         emit(RoomNoteListState.EmptyList)
                     else emit(RoomNoteListState.Receive(list))
                 }

         }catch ( e : Exception){}
     }



}