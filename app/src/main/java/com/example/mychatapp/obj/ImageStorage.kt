package com.example.mychatapp.obj

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class ImageStorage {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    fun uploadImage(uri: Uri, userID: String, callback: (Boolean, String) -> Unit){
        val imageRef = storageRef.child("images/users/${userID}/profile.jpg")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            callback(true, "Upload successful")
        }.addOnFailureListener { exception ->
            callback(false, "Upload failed: $exception")
        }
    }



    fun getImage(userID: String, callback: (Uri?) -> Unit) {
        val imageRef = storageRef.child("images/users/${userID}/profile.jpg")

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            callback(uri)
        }.addOnFailureListener {
            callback(null)
        }
    }
}