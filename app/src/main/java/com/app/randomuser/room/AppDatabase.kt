package com.app.randomuser.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.randomuser.models.Results
import com.app.randomuser.models.UserInfo

@Database(entities = [UserInfo::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterClass::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resultsDao(): DaoClass

}