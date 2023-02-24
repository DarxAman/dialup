package com.dialupdelta.ui.sleep_enhancer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.*
import com.dialupdelta.`interface`.AudioClickListener
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.AudioFileList


class SleepEnhancerDialogAdapter(private val context: Context, private val audioClickListener: AudioClickListener, private val programList:ArrayList<AudioFileList>?): RecyclerView.Adapter<SleepEnhancerDialogAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =LayoutInflater.from(parent.context).inflate(R.layout.item_sleep_enhancer_dialog, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = programList?.get(position)
        holder.apply {
            programItemText.text = "A"
        }
    }

    override fun getItemCount(): Int {
        return programList?.size?:0
    }

    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val programItemText = view.findViewById(R.id.programItemText) as TextView
        private val parentLayout = view.findViewById(R.id.parentLayout) as ConstraintLayout

        init {
            parentLayout.setOnClickListener {
               audioClickListener.setOnAudioClickListener(bindingAdapterPosition)
            }

            parentLayout.setOnLongClickListener(OnLongClickListener {
                audioClickListener.setOnAudioLongClickListener(bindingAdapterPosition)
                false
            })
        }
    }
}