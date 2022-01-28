package com.app.randomuser.ui.randomUserFragments.viewmodels

import com.app.randomuser.models.UserInfo
import com.app.randomuser.room.RoomInstance
import com.app.randomuser.services.RandomUserInterface
import retrofit2.Response

class MainRepository(var retrofitInterface: RandomUserInterface) {


    suspend fun getUserList(number: Int): Response<UserInfo> {
        return retrofitInterface.getUserList(number)

    }

    suspend fun insertAll(userInfo: UserInfo?) {
        userInfo?.let {
            RoomInstance.getResultDao().insert(userInfo)
        }
    }

    suspend fun getAllResults(): UserInfo {
        return RoomInstance.getResultDao().getAllResults()
    }


}