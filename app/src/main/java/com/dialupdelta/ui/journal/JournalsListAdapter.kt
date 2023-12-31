package com.dialupdelta.ui.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dialupdelta.R
import com.dialupdelta.`interface`.JournalEventListener
import com.dialupdelta.data.network.response.get_journal_response.JournalList

class JournalsListAdapter(
    private val journalEventListener: JournalEventListener,
    private val journalList: ArrayList<JournalList>?
) : RecyclerView.Adapter<JournalsListAdapter.ViewHolder>() {

    private var filteredJournalList: ArrayList<JournalList>? = ArrayList(journalList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_journals, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal = filteredJournalList?.get(position)
        holder.apply {
            recyTitle.text = journal?.title
            recyContent.text = journal?.content
            recyDate.text = journal?.saved_at
        }
    }

    fun filter(query: String) {
        filteredJournalList?.clear()
        if (query.isEmpty()) {
            // If the query is empty, show all items
            if (journalList != null) {
                filteredJournalList?.addAll(journalList)
            }
        } else {
            // If the query is not empty, filter items that match the query
            for (journal in journalList.orEmpty()) {
                if (journal.title.contains(query, true) || journal.content.contains(query, true)) {
                    filteredJournalList?.add(journal)
                }
            }
        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
       return filteredJournalList?.size?:0
    }

    fun removeJournalList(position: Int){
        journalList?.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, journalList?.size?:0)
    }

   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val recyclerCheck = itemView.findViewById<CheckBox>(R.id.recyCheck)
        val recyTitle = itemView.findViewById<TextView>(R.id.recyTitle)
        val recyContent = itemView.findViewById<TextView>(R.id.recyContent)
        val recyDate = itemView.findViewById<TextView>(R.id.recyDate)
        var clayoutRecycler = itemView.findViewById<ConstraintLayout>(R.id.clayoutRecycler)
        var cardList = itemView.findViewById<CardView>(R.id.cardList)

       init {
           clayoutRecycler.setOnClickListener {
               journalEventListener.journalItemClickListener(bindingAdapterPosition)
           }

           clayoutRecycler.setOnLongClickListener {
               journalEventListener.journalItemRemoveClickListener(bindingAdapterPosition)
               false
           }
       }
    }
}