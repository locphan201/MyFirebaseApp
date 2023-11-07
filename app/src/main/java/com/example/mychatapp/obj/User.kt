package com.example.mychatapp.obj

class User {
    private var uid: String = ""
    private var name: String = "ABC"
    private var roomIDs: ArrayList<String> = ArrayList()

    constructor() {}

    fun getUID() : String {
        return this.uid
    }

    fun setUID(uid: String) {
        this.uid = uid
    }

    fun getName() : String {
        return this.name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getRoomIDs() : ArrayList<String> {
        return this.roomIDs
    }

    fun setRoomIDs(roomIDs: ArrayList<String>) {
        this.roomIDs = roomIDs
    }

    fun addNewRoom(roomID: String) {
        this.roomIDs.add(roomID)
    }

    fun clear() {
        this.uid = ""
        this.name = ""
        this.roomIDs.clear()
    }
}