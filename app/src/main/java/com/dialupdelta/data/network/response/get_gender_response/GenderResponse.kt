package com.dialupdelta.data.network.response.get_gender_response

import com.google.gson.annotations.SerializedName

data class GenderResponse(

    @SerializedName("status") val status : Int,
    @SerializedName("msg") val msg : String,
    @SerializedName("data") val data : List<Gender>
)

data class Gender (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("is_active") val is_active : Int,
    @SerializedName("status") val status : String,
    @SerializedName("updated_at") val updated_at : String
)

{
    override fun toString(): String {
        return name
    }
}
