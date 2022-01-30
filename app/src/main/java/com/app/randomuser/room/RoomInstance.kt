package com.app.randomuser.room

import android.app.Application
import androidx.room.Room

object RoomInstance {
    private lateinit var appDatabase: AppDatabase

    fun initRoom(context: Application) {
        appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "resultDb").build()
    }


    fun getResultDao(): DaoClass {
        return appDatabase.resultsDao()
    }
}