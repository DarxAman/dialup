package com.dialupdelta.data.network.response.get_journal_response

import com.google.gson.annotations.SerializedName

data class GetSingleJournalList(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : SingleJournalList
)

data class SingleJournalList (
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String,
    @SerializedName("saved_at") val saved_at : String
)
