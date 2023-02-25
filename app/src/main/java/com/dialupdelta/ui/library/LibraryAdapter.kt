package com.dialupdelta.ui.library

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.`interface`.LibraryItemClickListener
import com.dialupdelta.data.network.response.get_library_response.LibraryModelList
import com.dialupdelta.utils.setUserImage
import java.io.Serializable

class LibraryAdapter(private val context: Context, private val libraryItemClickListener: LibraryItemClickListener, private val libraryModelList: List<LibraryModelList>?) : RecyclerView.Adapter<LibraryAdapter.MyHolder>(), Serializable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(context).inflate(R.layout.libraries, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val allList = libraryModelList?.get(position)
        holder.imageLibrary.setUserImage(context, allList?.thumbnail_url)
    }

    override fun getItemCount(): Int {
        return libraryModelList?.size?:0
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val imageLibrary = itemView.findViewById(R.id.imageLibrary) as ImageView

        init {
           imageLibrary.setOnClickListener {
               libraryItemClickListener.setOnLibraryItemClick(bindingAdapterPosition)
           }
        }
    }

}
