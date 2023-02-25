package com.dialupdelta.data.network.response.get_to_sleep_response

import com.google.gson.annotations.SerializedName

data class GetToSleepListResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : GetToSleepVideoData
)

data class GetToSleepVideoData (
    @SerializedName("list") val list : List<GetToSleep>,
    @SerializedName("base_url_thumb") val base_url_thumb : String,
    @SerializedName("base_url_video") val base_url_video : String
)


data class GetToSleep (
    @SerializedName("id") val id : Int,
    @SerializedName("thumb_url") val thumb_url : String,
    @SerializedName("video_url") val video_url : String
)
