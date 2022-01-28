package com.app.randomuser.room

import androidx.room.TypeConverter
import com.app.randomuser.models.Results
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class TypeConverterClass {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromResults(value: ArrayList<Results>): String {
            val typeItem = object : TypeToken<ArrayList<Results?>?>() {}.type
            return Gson().toJson(value, typeItem)
        }

        @TypeConverter
        @JvmStatic
        fun toResults(value: String): ArrayList<Results> {
            val typeItem = object : TypeToken<ArrayList<Results?>?>() {}.type
            return Gson().fromJson(value, typeItem)
        }

        @TypeConverter
        @JvmStatic
        fun fromInfo(value: Results.Info): String {
            return Gson().toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toInfo(value: String): Results.Info {
            return Gson().fromJson(value, Results.Info::class.java)
        }


    }
}