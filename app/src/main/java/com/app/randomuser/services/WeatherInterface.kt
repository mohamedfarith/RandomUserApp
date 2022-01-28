package com.app.randomuser.services

import com.app.randomuser.models.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {


    @GET("/data/2.5/weather")
    suspend fun getCurrentClimateData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Response<WeatherData>
}