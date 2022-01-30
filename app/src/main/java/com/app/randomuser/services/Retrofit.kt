package com.app.randomuser.services

import com.app.randomuser.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    fun getInstance(): RandomUserInterface {
        return apiInterface
    }

    fun getWeatherInstance(): WeatherInterface {
        return weatherInterface
    }

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    }

    private fun getWeatherRetrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private var weatherInterface: WeatherInterface =
        getWeatherRetrofitInstance().create(WeatherInterface::class.java)

    private var apiInterface: RandomUserInterface =
        getRetrofitInstance().create(RandomUserInterface::class.java)

}