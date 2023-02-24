package com.dialupdelta.data.network.response.otp_response

import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result?
)

 class Result{

 }
