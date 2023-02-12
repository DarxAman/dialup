package com.dialupdelta.`interface`

import com.dialupdelta.ui.library.LibraryModel

interface LibraryItemClickListener {
     fun setOnLibraryItemClick(position: Int, list:LibraryModel)
}