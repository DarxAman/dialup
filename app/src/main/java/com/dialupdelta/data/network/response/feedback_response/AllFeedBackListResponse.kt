package com.dialupdelta.data.network.response.feedback_response

import com.google.gson.annotations.SerializedName

data class AllFeedBackListResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : List<AllFeedBackList>
)

data class AllFeedBackList (
    @SerializedName("forDate") val forDate : String,
    @SerializedName("program_1") val program_1 : String,
    @SerializedName("duration_1") val duration_1 : String,
    @SerializedName("program_2") val program_2 : String,
    @SerializedName("duration_2") val duration_2 : String,
    @SerializedName("day") val day : String,
    @SerializedName("totalSlept") val totalSlept : Int,
    @SerializedName("cycleCount") val cycleCount : Int
)