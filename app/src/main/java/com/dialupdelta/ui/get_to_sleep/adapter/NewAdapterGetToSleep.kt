package com.dialupdelta.ui.get_to_sleep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.`interface`.ProgramClickPosition
import com.dialupdelta.utils.setUserImage

class NewAdapterGetToSleep(
    private var context: Context? = null,
    private var programId: List<String?>? = null,
    private var programName: List<String?>? = null,
    private var gender: String? = null,
    private var duration: String? = null,
    private var programClickPosition: ProgramClickPosition? = null,
    private var programPic: List<String?>? = null) : RecyclerView.Adapter<NewAdapterGetToSleep.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.gridsubjects, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val programPic = programPic?.get(position)
        context?.let { holder.gridImageView.setUserImage(it, programPic.toString()) }
    }

    override fun getItemCount(): Int {
        return 4
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
       val gridAdapter = view.findViewById<ConstraintLayout?>(R.id.cl_grid_adapter)
       val  gridImageView = view.findViewById(R.id.gridIMageView) as ImageView
    }
}