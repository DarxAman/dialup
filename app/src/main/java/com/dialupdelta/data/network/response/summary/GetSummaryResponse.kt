package com.dialupdelta.data.network.response.summary

import com.google.gson.annotations.SerializedName

data class GetSummaryResponse(
    @SerializedName("valid") val valid : Boolean,
    @SerializedName("status") val status : String,
    @SerializedName("data") val data : List<SummaryList>,
    @SerializedName("code") val code : Int
)

data class SummaryList (
    @SerializedName("id") val id : Int,
    @SerializedName("question_text") val question_text : String,
    @SerializedName("question_title") val question_title : String,
    @SerializedName("low_data_point") val low_data_point : String,
    @SerializedName("high_data_point") val high_data_point : String,
    @SerializedName("question_category_id") val question_category_id : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)
