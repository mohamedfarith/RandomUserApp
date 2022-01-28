package com.app.randomuser.ui.weatherAppFragments.viewModels

import com.app.randomuser.Constants
import com.app.randomuser.models.WeatherData
import com.app.randomuser.services.Resource
import com.app.randomuser.services.WeatherInterface
import retrofit2.Response

class RepositoryClass(private var retrofitInterface: WeatherInterface) {

    suspend fun getData(lat: Double, lon: Double): Response<WeatherData> {

        return retrofitInterface.getCurrentClimateData(lat, lon, "metric", Constants.API_KEY)
    }
}