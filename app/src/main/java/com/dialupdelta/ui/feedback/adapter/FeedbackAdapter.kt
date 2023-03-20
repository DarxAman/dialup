package com.dialupdelta.ui.feedback.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.*
import com.dialupdelta.`interface`.FeedBackItemClickListener
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry


class FeedbackAdapter(private val context: Context, private val feedBackItemClickListener: FeedBackItemClickListener, private val candleList:ArrayList<CandleEntry>) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setCandleStickChart(holder.candleStickChart)
    }

    private fun setCandleStickChart(candleStickChart: CandleStickChart) {
        val set1 = CandleDataSet(candleList, "DataSet 1")
        set1.color = Color.rgb(80, 80, 80);
        set1.shadowColor = context.resources.getColor(R.color.grey)
        set1.shadowWidth = 0.8f
        set1.decreasingColor = context.resources.getColor(R.color.color_extraversion)
        set1.decreasingPaintStyle = Paint.Style.FILL
        set1.increasingColor = context.resources.getColor(R.color.colorAccent)
        set1.increasingPaintStyle = Paint.Style.FILL
        set1.neutralColor = Color.LTGRAY
        set1.setDrawValues(false)
        val data = CandleData(set1)
        candleStickChart.data = data
    }

    override fun getItemCount(): Int {
        return 5
    }

   inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val candleStickChart = view.findViewById(R.id.candleStickChart) as CandleStickChart

       init {
           candleStickChart.setOnClickListener {
              feedBackItemClickListener.feedBackItemListener()
           }
       }
    }
}