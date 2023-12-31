package com.dialupdelta.ui.wakeup

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
import com.dialupdelta.data.network.response.wake_up_response.WakeUpProgramList
import com.dialupdelta.utils.setUserImage

class WakeUpAdapter(private var context: Context, private val getToSleepClickListener: GetToSleepClickListener, private val wakeUpProgramList:ArrayList<WakeUpProgramList>?) : RecyclerView.Adapter<WakeUpAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_get_to_sleep_program_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wakeUpProgram = wakeUpProgramList?.get(position)
        holder.apply {
            programItemText.text = wakeUpProgram?.program_name
            if(wakeUpProgram?.thumb?.isNullOrEmpty() == false) {
                Glide.with(context).load(wakeUpProgram?.thumb).placeholder(R.drawable.logo_toolbar)
                    .into(getToSleep_image)
            }

            val isActive = wakeUpProgram?.selected == 1
            if (isActive) {
                holder.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCB"))
            } else {
                holder.parentLayout.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    override fun getItemCount(): Int {
        return wakeUpProgramList?.size?:0
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