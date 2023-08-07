package com.dialupdelta.data.network.response.sleep_enhancer_list_response

import com.google.gson.annotations.SerializedName

data class SleepEnhancerResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : AudioDataList
)

data class AudioDataList (
    @SerializedName("list") val list : ArrayList<SleepEnhancerItem>,
    @SerializedName("saved_audios") val list_data : ArrayList<AudioFileList>,
    @SerializedName("base_url_mp3") val base_url_mp3 : String,
    @SerializedName("base_url_media") val base_url_image : String
)

data class SleepEnhancerItem(
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: String,
    @SerializedName("description") val description: String,
    @SerializedName("graph_image") val graph_image: String,
    @SerializedName("file_name")  val file_name: String
)

data class AudioFileList (
    @SerializedName("audio_1") val audio_1 : String,
    @SerializedName("audio_2") val file_name : String
)