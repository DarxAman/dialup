package com.dialupdelta.data.network.response.wake_up_response

import com.google.gson.annotations.SerializedName

data class WakeUpResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : WakeUp
)

data class WakeUp (
    @SerializedName("list") val list : List<WakeUpList>,
    @SerializedName("base_url_image") val base_url_image : String,
    @SerializedName("base_url_video") val base_url_video : String
)

data class WakeUpList (

    @SerializedName("id") val id : Int,
    @SerializedName("program") val program : Int,
    @SerializedName("gender") val gender : Int,
    @SerializedName("main_custom_video") val main_custom_video : String,
    @SerializedName("main_video") val main_video : String,
    @SerializedName("main_thumb") val main_thumb : String,
    @SerializedName("sub1Url") val sub1Url : String,
    @SerializedName("sub2Url") val sub2Url : String,
    @SerializedName("sub3Url") val sub3Url : String,
    @SerializedName("subOneName") val subOneName : String,
    @SerializedName("subTwoName") val subTwoName : String,
    @SerializedName("subThreeName") val subThreeName : String,
    @SerializedName("thumb1") val thumb1 : String,
    @SerializedName("thumb2") val thumb2 : String,
    @SerializedName("thumb3") val thumb3 : String,
    @SerializedName("language") val language : Int,
    @SerializedName("is_active") val is_active : Int,
    @SerializedName("status") val status : String,
    @SerializedName("updated_at") val updated_at : String
)


