package com.dialupdelta.data.network.response.get_to_sleep_response

import com.google.gson.annotations.SerializedName

data class SaveGetToSleepResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : SaveGetToSleep
)

data class SaveGetToSleep (
    @SerializedName("gender_id") val gender_id : Int,
    @SerializedName("duration_id") val duration_id : Int,
    @SerializedName("program_id") val program_id : Int,
    @SerializedName("video_id") val video_id : Int
)

