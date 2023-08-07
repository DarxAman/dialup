package com.dialupdelta.data.network.response.sleep_enhancer_list_response
import com.google.gson.annotations.SerializedName

data class SavedSleepEnhancerResponse(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("msg") val msg: String = "",
    @SerializedName("result") val result: SavedSleepEnhancer = SavedSleepEnhancer()
)

data class SavedSleepEnhancer(
    @SerializedName("t_1") val t_1: String = "",
    @SerializedName("t_2") val t_2: String = "",
    @SerializedName("duration_1") val duration_1: String = "",
    @SerializedName("duration_2") val duration_2: String = "",
    @SerializedName("program_1") val program_1: String = "",
    @SerializedName("program_2") val program_2: String = "",
    @SerializedName("audio_1") val audio_1: String = "",
    @SerializedName("audio_2") val audio_2: String = "",
    @SerializedName("volume") val volume: String = "",
    @SerializedName("user_id") val user_id: Int = 0
)
