package com.app.randomuser.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class WeatherData(
    @SerializedName("coord") var coord: Coord?,
    @SerializedName("weather") var weather: ArrayList<Weather>?,
    @SerializedName("base") var base: String?,
    @SerializedName("main") var main: Main?,
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("wind") var wind: Wind?,
    @SerializedName("clouds") var clouds: Clouds?,
    @SerializedName("dt") var dt: Int,
    @SerializedName("sys") var sys: Sys?,
    @SerializedName("timezone") var timezone: Int,
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String?,
    @SerializedName("cod") var cod: Int
) {
    fun getDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return simpleDateFormat.format(Date(dt * 1000.toLong()))
    }

    data class Coord(
        @SerializedName("lon") var lon: Double,
        @SerializedName("lat") var lat: Double
    )

    data class Weather(
        @SerializedName("id") var id: Int,
        @SerializedName("main") var main: String?,
        @SerializedName("description") var description: String?,
        @SerializedName("icon") var icon: String?
    )

    data class Main(
        @SerializedName("temp") var temp: Double,
        @SerializedName("feels_like") var feels_like: Double,
        @SerializedName("temp_min") var temp_min: Double,
        @SerializedName("temp_max") var temp_max: Double,
        @SerializedName("pressure") var pressure: Double,
        @SerializedName("humidity") var humidity: Double,
    ) {
        fun getTemperature(): String {
            return java.lang.String.valueOf(temp)
        }

        fun getMinimumTemp(): String {
            return "Minimum Temp: $temp_min"
        }

        fun getMaximumTemp(): String {
            return "Maximum Temp: $temp_max"
        }

        fun getHumidityValue(): String {
            return "$humidity %"
        }

        fun getPressureValue(): String {
            return "$pressure hpa"
        }
    }

    data class Wind(
        @SerializedName("speed") var speed: Double,
        @SerializedName("deg") var deg: Int
    ) {

        fun getSpeedValue(): String {
            return "$speed km/hr"
        }
    }

    data class Clouds(
        @SerializedName("all") var all: Int
    )

    data class Sys(
        @SerializedName("type") var type: Int,
        @SerializedName("id") var id: Int,
        @SerializedName("country") var country: String?,
        @SerializedName("sunrise") var sunrise: Int,
        @SerializedName("sunset") var sunset: Int
    ) {
        fun getSunriseData(): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
            return simpleDateFormat.format(Date(sunrise * 1000.toLong()))
        }

        fun getSunsetData(): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
            return simpleDateFormat.format(Date(sunset * 1000.toLong()))
        }


    }
}




