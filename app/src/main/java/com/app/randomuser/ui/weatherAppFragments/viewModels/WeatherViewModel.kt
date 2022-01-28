package com.app.randomuser.ui.weatherAppFragments.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.randomuser.models.WeatherData
import com.app.randomuser.services.Resource
import com.app.randomuser.services.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel : ViewModel() {
    private var weatherRepo: RepositoryClass = RepositoryClass(Retrofit.getWeatherInstance())
    private var reportData: MutableLiveData<Resource<WeatherData>> = MutableLiveData()


    fun getWeatherReport(
        latitude: Double,
        longitude: Double
    ): MutableLiveData<Resource<WeatherData>> {
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