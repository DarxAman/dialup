package com.dialupdelta.data.network.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

    @SerializedName("status") val status : Int,
    @SerializedName("msg") val msg : String,
    @SerializedName("data") val data : List<String>
)
