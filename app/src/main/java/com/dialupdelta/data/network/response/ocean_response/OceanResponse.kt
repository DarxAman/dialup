package com.dialupdelta.data.network.response.ocean_response

import com.google.gson.annotations.SerializedName

data class OceanResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : OceanData
)

data class OceanData (
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("percentage") val percentage : Int,
    @SerializedName("high_description") val high_description : String,
    @SerializedName("low_description") val low_description : String
)
