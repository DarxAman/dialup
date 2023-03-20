package com.dialupdelta.ui.feedback.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.data.network.response.feedback_response.AllFeedBackList

class AllFeedBackListAdapter(private val context: Context, private val allFeedBackList:ArrayList<AllFeedBackList>?):RecyclerView.Adapter<AllFeedBackListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_all_feedback_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allFeedBack = allFeedBackList?.get(position)
        holder.program1.text = "Program1: ${allFeedBack?.program_1}"
        holder.program2.text = "Program2: ${allFeedBack?.program_2}"
        holder.duration1.text = "Duration1: ${allFeedBack?.duration_1}"
        holder.duration2.text = "Duration2: ${allFeedBack?.duration_2}"
        holder.day.text = "day: ${allFeedBack?.day}"
        holder.totalSlept.text = "TotalSlept: ${allFeedBack?.totalSlept}"
        holder.cycleCount.text = "CycleCount: ${allFeedBack?.cycleCount}"
    }

    override fun getItemCount(): Int {
        return allFeedBackList?.size?:0
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val program1 = view.findViewById(R.id.program1) as TextView
        val program2 = view.findViewById(R.id.program2) as TextView
        val duration1 = view.findViewById(R.id.duration1) as TextView
        val duration2 = view.findViewById(R.id.duration2) as TextView
        val day = view.findViewById(R.id.day) as TextView
        val totalSlept = view.findViewById(R.id.totalSlept) as TextView
        val cycleCount = view.findViewById(R.id.cycleCount) as TextView
    }
}