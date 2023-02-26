package com.dialupdelta.data.network.response.sleep_enhancer_list_response

import com.google.gson.annotations.SerializedName

data class SavedSleepEnhancerResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : SavedSleepEnhancer
)

data class SavedSleepEnhancer (
    @SerializedName("duration") val duration : Int,
    @SerializedName("program_id") val program_id : Int,
    @SerializedName("audio_id") val audio_id : Int
)

