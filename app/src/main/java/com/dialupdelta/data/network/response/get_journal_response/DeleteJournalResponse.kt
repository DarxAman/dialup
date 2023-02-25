package com.dialupdelta.data.network.response.get_journal_response

import com.google.gson.annotations.SerializedName

data class DeleteJournalResponse(

    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : DeleteJournal
)

 class DeleteJournal {

 }
