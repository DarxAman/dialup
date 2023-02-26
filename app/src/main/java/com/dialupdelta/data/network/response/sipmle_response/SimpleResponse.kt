package com.dialupdelta.data.network.response.sipmle_response

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result
)

 class Result{

 }
