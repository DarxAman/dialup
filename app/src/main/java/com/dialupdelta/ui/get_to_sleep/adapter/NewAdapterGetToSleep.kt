package com.dialupdelta.ui.get_to_sleep.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dialupdelta.R
import com.dialupdelta.`interface`.GetToSleepClickListener
import com.dialupdelta.`interface`.ProgramClickPosition
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepList
import com.dialupdelta.utils.setUserImage

class NewAdapterGetToSleep(private var context: Context,private val getToSleepClickListener: GetToSleepClickListener, private val getToSleepList:ArrayList<GetToSleepList>?) : RecyclerView.Adapter<NewAdapterGetToSleep.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_get_to_sleep_program_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sleepList = getToSleepList?.get(position)
        holder.apply {
            programItemText.text = sleepList?.program_name

            if(sleepList?.thumb?.isNullOrEmpty() == false) {
                Glide.with(context).load(sleepList?.thumb).placeholder(R.drawable.logo_toolbar)
                    .into(getToSleep_image)
            }
        }

        val isActive = getToSleepList?.get(position)?.is_active == 1
        if (isActive) {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCB"))
        } else {
            holder.parentLayout.setBackgroundColor(Color.TRANSPARENT)
        }

    }

    override fun getItemCount(): Int {
        return getToSleepList?.size?:0
    }

   inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val programItemText = view.findViewById(R.id.programItemText) as TextView
        val parentLayout = view.findViewById(R.id.parentLayout) as ConstraintLayout
        val getToSleep_image = view.findViewById(R.id.getToSleep_image) as ImageView

       init {
           parentLayout.setOnClickListener {
               getToSleepClickListener.getToSleepProgramItemListener(bindingAdapterPosition)
           }
       }
    }
}