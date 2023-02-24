package com.dialupdelta.data.network.response.login_response

import com.google.gson.annotations.SerializedName

data class SignUpLoginResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : AuthData?
)

data class AuthData (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("language_id") val language_id : Int,
    @SerializedName("gender_id") val gender_id : Int,
    @SerializedName("age_group_id") val age_group_id : Int
)