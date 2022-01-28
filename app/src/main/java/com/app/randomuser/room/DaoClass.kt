package com.app.randomuser.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.randomuser.models.UserInfo

@Dao
interface DaoClass {
    @Query("SELECT * FROM userinfo")
    suspend fun getAllResults():UserInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userInfo: UserInfo)

//    @Query("SELECT * from contact WHERE firstName OR lastName LIKE '%' || :query || '%'")
//    fun findContactByName(query: String): LiveData<List<Contact>>
//
//    @Query("SELECT * FROM contact WHERE id = :contactId")
//    fun getContactById(contactId: Int): LiveData<Contact?>
}