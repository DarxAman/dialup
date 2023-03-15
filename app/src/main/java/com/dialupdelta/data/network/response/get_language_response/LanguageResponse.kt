package com.dialupdelta.data.network.response.get_language_response

import com.google.gson.annotations.SerializedName

data class LanguageResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result
)

data class Result (
    @SerializedName("languageData") val languageData : List<LanguageData>
)

data class LanguageData (
    @SerializedName("id") val id : Int,
    @SerializedName("slug") val slug : String,
    @SerializedName("language_name") val language_name : String

)

{
    override fun toString(): String {
        return language_name
    }
}
