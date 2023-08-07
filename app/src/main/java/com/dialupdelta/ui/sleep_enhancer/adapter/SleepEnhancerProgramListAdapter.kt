package com.dialupdelta.ui.sleep_enhancer.adapter

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
import com.dialupdelta.`interface`.ProgramListListener
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.ProgramList

class SleepEnhancerProgramListAdapter(private val context: Context, private val programList:ArrayList<ProgramList>?, private val programListListener: ProgramListListener): RecyclerView.Adapter<SleepEnhancerProgramListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =LayoutInflater.from(parent.context).inflate(R.layout.item_sleep_enhancer_program_layout, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = programList?.get(position)
        holder.apply {
            programItemText.text = program?.program_name
        }

        holder.parentLayout.setOnClickListener{
            programListListener.setOnProgramItemClickListener(position, program?.thumb)
        }

        val isActive = (programList?.get(position)?.is_active == 1)
        if (isActive) {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCB"))
        } else {
            holder.parentLayout.setBackgroundColor(Color.TRANSPARENT)
        }

        Glide.with(context).load(program?.thumb).into(holder.imageViewSleepProgram)
    }

    override fun getItemCount(): Int {
        return programList?.size?:0
    }

   inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
       val programItemText = view.findViewById(R.id.programItemText) as TextView
       val parentLayout = view.findViewById(R.id.parentLayout) as ConstraintLayout
       val imageViewSleepProgram = view.findViewById(R.id.imageViewSleepProgram) as ImageView

//       init {
//           parentLayout.setOnClickListener {
//               programListListener.setOnProgramItemClickListener(bindingAdapterPosition)
//           }
//       }
    }

}