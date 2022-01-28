package com.app.randomuser.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class UserInfo(
    @SerializedName("results") @ColumnInfo(name = "results") var resultsList: ArrayList<Results>?,
    @SerializedName("info") @ColumnInfo(name = "info") var info: Results.Info?,
    @PrimaryKey(autoGenerate = true)
    var id: Long
) : Serializable