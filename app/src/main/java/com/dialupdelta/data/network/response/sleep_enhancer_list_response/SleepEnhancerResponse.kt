package com.dialupdelta.data.network.response.sleep_enhancer_list_response

import com.google.gson.annotations.SerializedName

data class SleepEnhancerResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : AudioDataList
)

data class AudioDataList (
    @SerializedName("list") val list : ArrayList<AudioFileList>,
    @SerializedName("base_url") val base_url : String
)

data class AudioFileList (
    @SerializedName("id") val id : Int,
    @SerializedName("file_name") val file_name : String
)