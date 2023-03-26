package com.dialupdelta.data.network.response.wake_up_response

import com.google.gson.annotations.SerializedName

data class FetchWakeUpSavedResponse(

    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : FetchWakeUpSaved
)

data class FetchWakeUpSaved (
    @SerializedName("gender") val gender : Int,
    @SerializedName("program") val program : Int,
    @SerializedName("thumbURL") val thumbURL : String,
    @SerializedName("videoURL") val videoURL : String,
    @SerializedName("time") val time : String,
    @SerializedName("repeatDays") val repeatDays : String
)
