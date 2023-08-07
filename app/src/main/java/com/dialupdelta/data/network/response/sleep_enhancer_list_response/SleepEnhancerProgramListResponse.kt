package com.dialupdelta.data.network.response.sleep_enhancer_list_response

import com.google.gson.annotations.SerializedName

data class SleepEnhancerProgramListResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : Result
)

data class Result (
    @SerializedName("list") val list : List<ProgramList>
)

data class ProgramList (
    @SerializedName("id") val id : Int,
    @SerializedName("program_name") val program_name : String,
    @SerializedName("is_active") val is_active : Int,
    @SerializedName("thumb") val thumb : String
)
