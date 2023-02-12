package com.dialupdelta.data.network.response.get_gender_response

import com.google.gson.annotations.SerializedName

data class AgeGroup (
    @SerializedName("id") val id : Int,
    @SerializedName("age_group_name") val age_group_name : String
){
    override fun toString(): String {
        return age_group_name
    }
}