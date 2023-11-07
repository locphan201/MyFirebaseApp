package com.example.mychatapp.obj

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreDB {
    private val db = Firebase.firestore

    fun addNewUser(uid: String, name: String) {
        val user = hashMapOf(
            "id" to uid,
            "name" to name,
            "roomIDs" to ArrayList<String>()
        )

        db.collection("users").add(user)
    }

    fun getUser(userID: String, onComplete: (DocumentSnapshot?) -> Unit) {
        val usersCollection = db.collection("users")

        val userQuery = usersCollection.whereEqualTo("id", userID).limit(1)

        userQuery.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    onComplete(document)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun updateUsername(userID: String, newData: String, onComplete: (Boolean, String) -> Unit) {
        val usersCollection = db.collection("users")

        val userQuery = usersCollection.whereEqualTo("id", userID).limit(1)

        userQuery.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.update("name", newData)
                        .addOnSuccessListener {
                            onComplete(true, "Updated successfully.")
                        }
                        .addOnFailureListener { e ->
                            onComplete(false, "Error updating username: $e")
                        }
                }
            }
            .addOnFailureListener { exception ->
                onComplete(false, "Error getting documents: $exception")
            }
    }


//    fun getUserList() {
//        db.collection("users")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//
//                }
//            }
//            .addOnFailureListener {
//
//            }
//    }

    fun addNewRoomID(userID: String, roomID: String, onComplete: (String) -> Unit) {
        val usersCollection = db.collection("users")

        val userQuery = usersCollection.whereEqualTo("id", userID).limit(1)

        userQuery.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val roomIDs = document["roomIDs"] as? ArrayList<String>
                    roomIDs?.add(roomID)

                    document.reference.update("roomIDs", roomIDs)
                        .addOnSuccessListener {
                            onComplete("Room ID added successfully.")
                        }
                        .addOnFailureListener { e ->
                            onComplete("Error adding room ID: $e")
                        }
                }
            }
            .addOnFailureListener { exception ->
                onComplete("Error getting documents: $exception")
            }
    }
}