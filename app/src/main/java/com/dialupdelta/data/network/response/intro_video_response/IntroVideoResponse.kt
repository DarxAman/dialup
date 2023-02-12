package com.dialupdelta.data.network.response.intro_video_response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IntroVideoResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : IntroVideo
):Serializable

data class IntroVideo (
    @SerializedName("videos") val videos : List<Videos>,
    @SerializedName("base_url") val base_url : String
):Serializable

data class Videos (
    @SerializedName("id") val id : Int,
    @SerializedName("video_file_name") val video_file_name : String
):Serializable
