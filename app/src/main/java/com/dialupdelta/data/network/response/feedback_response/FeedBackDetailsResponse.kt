package com.dialupdelta.data.network.response.feedback_response

import com.google.gson.annotations.SerializedName

data class FeedBackDetailsResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result
)

data class Result (
    @SerializedName("list") val list : List<FeedBackDetails>
)


data class FeedBackDetails (
    @SerializedName("startDate") val startDate : String,
    @SerializedName("endDate") val endDate : String,
    @SerializedName("totalSlept") val totalSlept : Int,
    @SerializedName("cycleCount") val cycleCount : Int
)
