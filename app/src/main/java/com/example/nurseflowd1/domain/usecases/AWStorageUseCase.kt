package com.example.nurseflowd1.domain.usecases

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.nurseflowd1.domain.AWCreds
import com.example.nurseflowd1.presentation.screens.accountmanage.ProfilePictureState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Arrays

class AWStorageUseCase(val client: Client, val context: Context) {

    val AppwriteStorage = Storage(client)
    val awcred = AWCreds()
    suspend fun getProfilePicture(fieldid : String) : MutableStateFlow<ProfilePictureState> {
        val profilepicstate  : MutableStateFlow<ProfilePictureState>  = MutableStateFlow(ProfilePictureState.empty)
        Log.d("TAGY", "Fetching $fieldid from Fs")
        try {
            val fetchfile = AppwriteStorage.getFileView(
                awcred.bucketid,
                fieldid
            )
            Log.d("AW", "Success fetched   file with ID $fieldid" +
                    " ${Arrays.toString(fetchfile)}")
            //Converts ByteArray in Bitmap
            val bitmap = BitmapFactory.decodeByteArray(fetchfile, 0 , fetchfile.size)
            profilepicstate.value = ProfilePictureState.Fetched(bitmap)
        }catch (e : AppwriteException) {
            Log.e("AW", "NO file with ID :  $fieldid ,  ${e.message}")
        }
        return profilepicstate
    }


    suspend fun postProfilePicture(uri: Uri, context: Context , path : DocumentReference , previousid : String) : MutableStateFlow<ProfilePictureState>  {
        var defaultstate :  MutableStateFlow<ProfilePictureState> = MutableStateFlow(ProfilePictureState.empty)
        var previousid = previousid

        val profileByteArray = convertUriToByteArray(uri, context)
        val filename = getFileNameFromUri(uri, context)
        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"

        try {
            if(previousid == "default"){
                val uploadedFile = AppwriteStorage.createFile(
                    bucketId = awcred.bucketid,
                    fileId = ID.unique(),
                    file = InputFile.fromBytes(
                        bytes = profileByteArray,
                        filename = filename,
                        mimeType = mimeType
                    )
                )
                previousid = uploadedFile.id
                Log.d("AW", "Success fully saved file with ID: ${uploadedFile.id}")
                // Save ProfilePicId in FB
                val newfeild = hashMapOf( "profilepicid" to uploadedFile.id)
                path.set(newfeild, SetOptions.merge()).await()
                defaultstate.value = ProfilePictureState.Added // Fetch From FireStore
            }else {
                // If there is Already A AppWrite File in FireStore , which is begin fetched to display in App
                //First Delete the Profile Picture in Appwrite using the Id from Firestore and then add new Picture
                try {
                    val deletefile = AppwriteStorage.deleteFile(
                        bucketId = awcred.bucketid,
                        fileId = previousid
                    )
                    // Add File
                    val uploadedFile = AppwriteStorage.createFile(
                        bucketId = awcred.bucketid,
                        fileId = ID.unique(),
                        file = InputFile.fromBytes(
                            bytes = profileByteArray,
                            filename = filename,
                            mimeType = mimeType
                        )
                    )
                    previousid = uploadedFile.id
                    Log.d("AW", "Success fully saved file with ID: ${uploadedFile.id}")
                    val newfeild = hashMapOf( "profilepicid" to uploadedFile.id)
                    path.set(newfeild, SetOptions.merge()).await()
                    defaultstate.value = ProfilePictureState.Added
                }catch (e: Exception) {
                    Log.e("AW", "Appwrite error: ${e.message}")
                    defaultstate.value = ProfilePictureState.failed(e.message!!)

                }

                // Save ProfilePicId in FB

            }
        }
        catch (e: Exception) {
            Log.e("AW", "Appwrite error: ${e.message}")
            defaultstate.value = ProfilePictureState.failed(e.message!!)

        }
        return defaultstate
    }

    suspend fun DeleteProfile(fileid : String , path : DocumentReference) : MutableStateFlow<ProfilePictureState> {
        var defaultstate :  MutableStateFlow<ProfilePictureState> = MutableStateFlow(ProfilePictureState.empty)
        // Firebase Id
        try { val deletefile = AppwriteStorage.deleteFile(
                bucketId = awcred.bucketid,
                fileId = fileid
            )
            val newfeild = hashMapOf( "profilepicid" to "default")
            path.set(newfeild, SetOptions.merge()).await()
            defaultstate.value = ProfilePictureState.Added
        } catch (e: Exception) {
            defaultstate.value = ProfilePictureState.failed(e.message!!)
            Log.e("AW", "Appwrite error: ${e.message}")
        }
      return  defaultstate
    }


    // Delete File























//
//    suspend fun getProfileFromAW(Authuid: String, context: Context): MutableStateFlow<Bitmap> {
//        val defaultBitmap = MutableStateFlow(
//            BitmapFactory.decodeResource(context.resources, R.drawable.profilepicture)
//        )
//        try {
//            android.util.Log.d("AW", "Attempting to fetch profile with ID: $Authuid")
//            val byteArray = AppwriteStorage.getFileView(
//                bucketId = "673b1d57000b839a42be",
//                fileId = Authuid
//            )
//            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//            defaultBitmap.value = bitmap
//        } catch (e: AppwriteException) {
//            android.util.Log.e("AW", "Error fetching profile: ${e.message}")
//        }
//        return defaultBitmap
//    }

    // Helper functions
    private suspend fun convertUriToByteArray(uri: Uri, context: Context): ByteArray = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use {
                it.readBytes()
            } ?: throw IllegalStateException("Could not open input stream for URI")
        }
    
    private fun getFileNameFromUri(uri: Uri, context: Context): String {
        var fileName = ""
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        return fileName.ifEmpty { "file_${System.currentTimeMillis()}" }
    }

    // Get file from Appwrite
}

