package com.app.randomuser.services

import com.app.randomuser.models.UserInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserInterface {

    @GET("/api")
    suspend fun getUserList(@Query("results") results: Int): Response<UserInfo>
}