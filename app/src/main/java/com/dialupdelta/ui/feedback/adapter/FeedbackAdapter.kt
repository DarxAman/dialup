package com.dialupdelta.ui.feedback.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.`interface`.FeedBackItemClickListener
import com.dialupdelta.data.network.response.feedback_response.FeedBackDetails

class FeedbackAdapter(private val context: Context, private val feedBackItemClickListener: FeedBackItemClickListener) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.image.setImageResource(R.drawable.sign_wave_image)
    }

    override fun getItemCount(): Int {
        return 1
    }

   inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val image = view.findViewById(R.id.imageGraph) as ImageView

       init {
           image.setOnClickListener {
              feedBackItemClickListener.feedBackItemListener()
           }
       }
    }
}