package com.dialupdelta.data.network.response.get_journal_response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetJournalList(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : List<JournalList>
)

data class JournalList (
    @SerializedName("id") val id : Int,
    @SerializedName("user_id") val user_id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String,
    @SerializedName("saved_at") val saved_at : String,
    @SerializedName("is_active") val is_active : Int,
    @SerializedName("updated_at") val updated_at : String
):Serializable
