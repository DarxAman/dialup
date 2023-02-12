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
import com.dialupdelta.utils.setUserImage
import java.io.Serializable

class LibraryAdapter(private val context: Context, private val libraryItemClickListener: LibraryItemClickListener, private val list: List<LibraryModel>) : RecyclerView.Adapter<LibraryAdapter.MyHolder>(), Serializable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(context).inflate(R.layout.libraries, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val allList = list.get(position)
        holder.imageLibrary.setUserImage(context, allList.thumb)

//        holder.imageLibrary.setOnClickListener { v: View? ->
//            val intent = Intent(context, LibraryInfo::class.java)
//            intent.putExtra("pos", list as Serializable)
//            intent.putExtra("newpos", position)
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val imageLibrary = itemView.findViewById(R.id.imageLibrary) as ImageView

        init {
           imageLibrary.setOnClickListener {
               libraryItemClickListener.setOnLibraryItemClick(bindingAdapterPosition, list.get(bindingAdapterPosition))
           }
        }
    }

}
