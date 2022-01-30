package com.app.randomuser.ui.weatherAppFragments.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.randomuser.models.WeatherData
import com.app.randomuser.services.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel : ViewModel() {
    private val weatherRepo: RepositoryClass by lazy {
        RepositoryClass()
    }
    private val reportData: MutableLiveData<Resource<WeatherData>> by lazy {
        MutableLiveData()
    }


    fun getWeatherReport(
        latitude: Double,
        longitude: Double
    ): MutableLiveData<Resource<WeatherData>> {
        reportData.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val response = weatherRepo.getData(latitude, longitude)

            withContext(Dispatchers.Main) {
                if (response.code() == 200 && response.isSuccessful) {
                    reportData.postValue(Resource.success(response.body()))
                } else {
                    reportData.postValue(Resource.error("Server error", null))
                }

            }
        }

        return reportData
    }


}