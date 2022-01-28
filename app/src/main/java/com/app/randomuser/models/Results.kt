package com.app.randomuser.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Results(
    @SerializedName("gender") @Expose var gender: String?,
    @SerializedName("name") @Expose var name: Name?,
    @SerializedName("location") @Expose var location: Location?,
    @SerializedName("email") @Expose var email: String?,
    @SerializedName("login") @Expose var login: Login?,
    @SerializedName("dob") @Expose var dob: Dob?,
    @SerializedName("registered") @Expose var registered: Registered?,
    @SerializedName("phone") @Expose var phone: String?,
    @SerializedName("cell") @Expose var cell: String?,
    @SerializedName("id") @Expose var id: Id?,
    @SerializedName("picture") @Expose var picture: Picture?,
    @SerializedName("nat") var nat: String?
) : Serializable {
    data class Name(
        @SerializedName("title") @Expose var title: String?,
        @SerializedName("first") @Expose var first: String?,
        @SerializedName("last") @Expose var last: String?

    ) : Serializable {
        fun getFullName(): String {
            return StringBuilder()
                .append(title)
                .append(" ").append(first).append(" ").append(last).toString()
        }
    }

    data class Street(
        @SerializedName("number") @Expose var number: Int,
        @SerializedName("name") @Expose var name: String?
    ) : Serializable {

        fun getStreetAddress(): String {
            return java.lang.StringBuilder().append(number).append(", ")
                .append(name).toString()
        }
    }

    data class Coordinates(
        @SerializedName("latitude") @Expose var latitude: String?,
        @SerializedName("longitude") @Expose var longitude: String?
    ) : Serializable

    data class Timezone(
        @SerializedName("offset") @Expose var offset: String?,
        @SerializedName("description") @Expose var description: String
    ) : Serializable

    data class Location(
        @SerializedName("street") @Expose var street: Street?,
        @SerializedName("city") @Expose var city: String?,
        @SerializedName("state") @Expose var state: String?,
        @SerializedName("country") @Expose var country: String?,
        @SerializedName("postcode") @Expose var postcode: String?,
        @SerializedName("coordinates") @Expose var coordinates: Coordinates?,
        @SerializedName("timezone") @Expose var timezone: Timezone?
    ) : Serializable {
        fun getFullAddress(): String {
            return java.lang.StringBuilder()
                .append(street?.getStreetAddress()).append(", ")
                .append(city).append(", ")
                .append(state).append("-").append(postcode).append(", ")
                .append(country).toString()
        }
    }

    data class Login(
        @SerializedName("uuid") @Expose var uuid: String?,
        @SerializedName("username") @Expose var username: String?,
        @SerializedName("password") @Expose var password: String?,
        @SerializedName("salt") @Expose var salt: String?,
        @SerializedName("md5") @Expose var md5: String?,
        @SerializedName("sha1") @Expose var sha1: String?,
        @SerializedName("sha256") @Expose var sha256: String?
    ) : Serializable {

        fun passwordToShow(): String? {
            val builder = java.lang.StringBuilder()
            password?.let {
                for (i in it.indices) {
                    builder.append("*")
                }
            }
            return if (builder.isEmpty()) {
                password
            } else
                builder.toString()
        }
    }

    data class Dob(
        @SerializedName("date") @Expose var date: String?,
        @SerializedName("age") @Expose var age: Int
    ) : Serializable

    data class Registered(
        @SerializedName("date") var date: String?,
        @SerializedName("age") var age: Int
    ) : Serializable

    data class Id(
        @SerializedName("name") var name: String?,
        @SerializedName("value") var value: String?,
    ) : Serializable

    data class Picture(
        @SerializedName("large") @Expose var large: String?,
        @SerializedName("medium") @Expose var medium: String?,
        @SerializedName("thumbnail") @Expose var thumbnail: String?
    ) : Serializable

    data class Info(
        @SerializedName("seed") @Expose var seed: String?,
        @SerializedName("age") @Expose var results: Int,
        @SerializedName("aaa") @Expose var page: Int,
        @SerializedName("version") @Expose var version: String?
    ) : Serializable
}