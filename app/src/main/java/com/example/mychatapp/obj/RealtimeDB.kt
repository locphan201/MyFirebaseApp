package com.example.mychatapp.obj

import com.google.firebase.database.FirebaseDatabase

class RealtimeDB {
    private val url = "https://my-chat-app-83f73-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val db = FirebaseDatabase.getInstance(url)

    fun createNewRoom(name: String, callback: (String?) -> Unit) {
        val newRef = db.getReference("/chatrooms").push()
        var roomName = name

        if (name == "") {
            roomName = "Room ${newRef.key.toString()}"
        }

        val roomInfo = hashMapOf(
            "name" to roomName
        )

        newRef.setValue(roomInfo)
            .addOnSuccessListener {
                callback(newRef.key.toString())
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getRoomInfo() {

    }

//    myRef.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            val value = dataSnapshot.getValue(String::class.java)
//            Log.d("TAG", "Value is: $value")
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.d("TAG", "Failed to read value.", error.toException())
//        }
//    })
}