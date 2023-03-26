package com.dialupdelta.ui.sleep_enhancer

import com.google.gson.annotations.SerializedName

data class LocalSaveSleepEnhancer(
    @SerializedName("t_1") var time1: String? = "45",
    @SerializedName("t_2") var time2: String? =  "135",
    @SerializedName("duration_1") var duration_1: String? = null,
    @SerializedName("duration_2") var duration_2: String? = null,
    @SerializedName("program_1") var program_1: String? = null,
    @SerializedName("program_2") var program_2: String? = null,
    @SerializedName("audio_1") var audio_1: String? = null,
    @SerializedName("audio_2") var audio_2: String? = null,
    @SerializedName("volume") var volume: String? = "1",
    @SerializedName("user_id") var user_id: String? = null
)
