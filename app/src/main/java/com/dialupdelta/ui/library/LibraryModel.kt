package com.dialupdelta.ui.library

import java.io.Serializable

class LibraryModel(
    var id: String,
    var nameVideo: String,
    var author: String,
    var duration: String,
    var thumb: String,
    var videoLink: String,
    var authorImg: String,
    var description: String
) : Serializable
