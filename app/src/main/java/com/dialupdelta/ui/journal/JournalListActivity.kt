package com.dialupdelta.ui.journal


import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.`interface`.JournalEventListener
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityJournalListBinding
import com.dialupdelta.utils.MyKeys
import org.kodein.di.generic.instance

class JournalListActivity : BaseActivity(), JournalEventListener {
    private lateinit var binding:ActivityJournalListBinding
    private val factory: JournalViewModelFactory by instance()
    private lateinit var viewModel: JournalViewModel
    private lateinit var journalsListAdapter: JournalsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_list)

        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[JournalViewModel::class.java]
        setObserver(viewModel)
        viewModel.getJournalList()
    }

    private fun setObserver(viewModel: JournalViewModel) {

        viewModel.successJournalList.observe(this){
             journalsListAdapter = JournalsListAdapter(this, viewModel.getAllJournalList())
            binding.recyclerViewJournals.adapter = journalsListAdapter
        }

        viewModel.deleteJournal.observe(this){
            journalsListAdapter.removeJournalList(it)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }

    override fun journalItemClickListener(position: Int) {
        Intent(this, AddJournalActivity::class.java).also {
            it.putExtra(MyKeys.journalList, viewModel.getAllJournalList()?.get(position))
            it.putExtra(MyKeys.shouldJournal, true)
            startActivity(it)
        }
    }

    override fun journalItemRemoveClickListener(position: Int) {
        val id = viewModel.getAllJournalList()?.get(position)?.id
        viewModel.deleteJournal(id, position)
    }
}