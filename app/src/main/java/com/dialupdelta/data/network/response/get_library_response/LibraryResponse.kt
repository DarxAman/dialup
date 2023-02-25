package com.dialupdelta.data.network.response.get_library_response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LibraryResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("msg") val msg : String,
    @SerializedName("result") val result : List<LibraryModelList>
)

data class LibraryModelList (
    @SerializedName("title") val title : String,
    @SerializedName("author_name") val author_name : String,
    @SerializedName("author_url") val author_url : String,
    @SerializedName("type") val type : String,
    @SerializedName("height") val height : Int,
    @SerializedName("width") val width : Int,
    @SerializedName("version") val version : Double,
    @SerializedName("provider_name") val provider_name : String,
    @SerializedName("provider_url") val provider_url : String,
    @SerializedName("thumbnail_height") val thumbnail_height : Int,
    @SerializedName("thumbnail_width") val thumbnail_width : Int,
    @SerializedName("thumbnail_url") val thumbnail_url : String,
    @SerializedName("html") val html : String,
    @SerializedName("mainlink") val mainlink : String
):Serializable


