package com.app.randomuser.ui.weatherAppFragments.viewModels

import com.app.randomuser.Constants
import com.app.randomuser.models.WeatherData
import com.app.randomuser.services.Retrofit
import retrofit2.Response

class RepositoryClass {

    suspend fun getData(lat: Double, lon: Double): Response<WeatherData> {
        return Retrofit.getWeatherInstance()
            .getCurrentClimateData(lat, lon, "metric", Constants.API_KEY)
    }
}