package com.dialupdelta.data.network.response.sleep_enhancer_list_response

import com.google.gson.annotations.SerializedName

data class SleepEnhancerProgramListResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : ArrayList<ProgramList>?
)

data class ProgramList (
    @SerializedName("id") val id : Int,
    @SerializedName("program_name") val program_name : String
)
