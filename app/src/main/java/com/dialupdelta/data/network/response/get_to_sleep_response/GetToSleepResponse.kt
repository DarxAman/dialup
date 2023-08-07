package com.dialupdelta.data.network.response.get_to_sleep_response

import com.google.gson.annotations.SerializedName

data class GetToSleepResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result
)

data class Result (
    @SerializedName("list") val list : List<GetToSleepList>
)

data class GetToSleepList (
    @SerializedName("id") val id : Int? = 1,
    @SerializedName("program_name") val program_name : String,
    @SerializedName("is_active") val is_active : Int,
    @SerializedName("thumb") val thumb : String
)