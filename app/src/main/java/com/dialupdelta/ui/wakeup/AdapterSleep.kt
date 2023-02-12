package com.dialupdelta.ui.wakeup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.`interface`.ClickInterface
import com.dialupdelta.`interface`.LongPressSleep2
import com.dialupdelta.utils.setUserImage

class AdapterSleep(
    private val context: Context,
    private val ids: List<String>,
    private val traint: List<String>,
    private val thumb: List<String>,
    private val duration: List<String>,
    private val url: List<String>,
    private var clickInterface: ClickInterface,
    private var longPressSleep2: LongPressSleep2,
    private var trait: String? = null
) : RecyclerView.Adapter<AdapterSleep.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.recyclersleep, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
       // Picasso.get().load(thumb[position]).fit().into(holder.sleep_videoview)
        holder.sleep_videoview.setUserImage(context, thumb[position])
        holder.sleep_videoview.setOnClickListener { v: View? ->
            clickInterface.urlGet(
                url[position],
                ""
            )
        }
        holder.sleep_videoview.setOnLongClickListener {
            Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show()
            longPressSleep2.longPressId(url[position], trait)
            true
        }
    }

    override fun getItemCount(): Int {
        return url.size
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sleep_videoview: ImageView

        init {
            sleep_videoview = itemView.findViewById(R.id.sleep_videoview)
        }
    }
}