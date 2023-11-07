package com.example.mychatapp.obj

import com.google.firebase.auth.FirebaseAuth

class Authentication {
    private val auth = FirebaseAuth.getInstance()

    // Function to handle user registration
    fun signUp(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid.toString()
                    val name = email.split("@")[0]
                    FireStoreDB().addNewUser(uid = uid, name = name)

                    onComplete(true, null) // Registration successful
                } else {
                    onComplete(false, task.exception?.message) // Registration failed, provide error message
                }
            }
    }

    // Function to handle user login
    fun signIn(email: String, password: String, onComplete: (User?, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userID = auth.currentUser?.uid.toString()
                    FireStoreDB().getUser(userID) { documentSnapshot ->
                        if (documentSnapshot != null) {
                            val data = documentSnapshot.data
                            val name = data?.get("name").toString()
                            val roomIDs = data?.get("roomIDs") as? ArrayList<String>

                            val user = User()
                            user.setUID(userID)
                            user.setName(name)
                            if (roomIDs != null) {
                                user.setRoomIDs(roomIDs)
                            }

                            onComplete(user, null)
                        } else {
                            onComplete(null, "Cannot get user information")
                        }
                    }
                } else {
                    onComplete(null, task.exception?.message)
                }
            }
    }

    // Function to handle user logout
    fun signOut(onComplete: (Boolean) -> Unit) {
        auth.signOut()
        onComplete(true)
    }
}
