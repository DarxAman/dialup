package com.dialupdelta.data.network.response.login_response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("msg") val msg : String,
    @SerializedName("data") val data : AuthData?
)

data class AuthData (
    @SerializedName("userId") val userId : Int?,
    @SerializedName("email") val email : String,
    @SerializedName("genderId") val genderId : Int,
    @SerializedName("languageId") val languageId : Int
)
