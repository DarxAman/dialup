package com.dialupdelta.data.network.response.wake_up_response

import com.google.gson.annotations.SerializedName

data class WakeUpProgramResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result?
)

data class Result (
    @SerializedName("list") val list : List<WakeUpProgramList>
)

data class WakeUpProgramList (
    @SerializedName("id") val id : Int,
    @SerializedName("program_name") val program_name : String,
    @SerializedName("selected") val selected : Int,
    @SerializedName("thumb") val thumb : String
)