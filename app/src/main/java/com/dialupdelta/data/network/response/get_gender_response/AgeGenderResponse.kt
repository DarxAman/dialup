package com.dialupdelta.data.network.response.get_gender_response

import com.google.gson.annotations.SerializedName

data class AgeGenderResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result
)

data class Result (
    @SerializedName("genderList") val genderList : List<GenderList>,
    @SerializedName("ageGroup") val ageGroup : List<AgeGroup>,
    @SerializedName("infoPageVideo") val infoPageVideo : String
)

data class GenderList (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)

{
    override fun toString(): String {
        return name
    }
}
