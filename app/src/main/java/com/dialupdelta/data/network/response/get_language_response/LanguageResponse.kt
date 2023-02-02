package com.dialupdelta.data.network.response.get_language_response

import com.google.gson.annotations.SerializedName

data class LanguageResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("msg") val msg : String,
    @SerializedName("data") val data : List<Data>
)

data class Data (
    @SerializedName("id") val id : Int,
    @SerializedName("slug") val slug : String,
    @SerializedName("language_name") val language_name : String,
    @SerializedName("is_active") val is_active : Int,
    @SerializedName("status") val status : String,
    @SerializedName("updatedAt") val updatedAt : String

)

{
    override fun toString(): String {
        return language_name
    }
}
