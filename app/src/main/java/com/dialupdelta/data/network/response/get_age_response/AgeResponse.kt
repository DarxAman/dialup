package com.dialupdelta.data.network.response.get_age_response

import com.google.gson.annotations.SerializedName

data class AgeResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("msg") val msg : String,
    @SerializedName("data") val data : List<AgeData>
)

data class AgeData (
    @SerializedName("id") val id : Int,
    @SerializedName("age_group_name") val age_group_name : String,
    @SerializedName("is_active") val is_active : Int,
    @SerializedName("status") val status : String
)
{
    override fun toString(): String {
        return age_group_name
    }
}
