package com.app.randomuser

import android.app.Application
import com.app.randomuser.room.RoomInstance

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RoomInstance.initRoom(this)
    }

}