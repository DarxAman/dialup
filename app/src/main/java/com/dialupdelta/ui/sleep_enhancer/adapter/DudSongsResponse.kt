package com.dialupdelta.ui.sleep_enhancer.adapter

// DudSongsResponse.kt

data class DudSongsResponse(
    val status: Boolean,
    val msg: String,
    val result: Result
)

data class Result(
    val data: List<DudSong>,
    val base_url: String
)

data class DudSong(
    val id: String,
    val filename: String,
    val song_name: String
)

