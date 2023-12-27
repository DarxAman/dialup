package com.dialupdelta.ui.journal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.data.network.response.get_journal_response.JournalList
import com.dialupdelta.databinding.ActivityAddJournalBinding
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.showToastMessage
import org.kodein.di.generic.instance
import java.util.*

class AddJournalActivity : BaseActivity() {
    private lateinit var binding:ActivityAddJournalBinding
    private val factory: JournalViewModelFactory by instance()
    private lateinit var viewModel: JournalViewModel
    private lateinit var   journalList:JournalList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_add_journal)
        initUI()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[JournalViewModel::class.java]
        setObserver(viewModel)

        val currentTime = Calendar.getInstance().time
        binding.jourDate.text = currentTime.toString()

        if (intent !=null && intent.hasExtra(MyKeys.shouldJournal)){
             journalList = intent.getSerializableExtra(MyKeys.journalList) as JournalList
            binding.jourTitle.setText(journalList.title)
            binding.jourContent.setText(journalList.content)
            binding.jourDate.text = journalList.saved_at
        }

        binding.jourSave.setOnClickListener {
          val jourTitle = binding.jourTitle.text.toString()
          val jourDate = binding.jourDate.text.toString()
          val jourContent = binding.jourContent.text.toString()
            if (jourTitle.isEmpty()){
                showToastMessage(this, "Please enter title")
            }
            else if(jourDate.isEmpty()){
                showToastMessage(this, "Please enter date")
            }

            else if(jourContent.isEmpty()){
                showToastMessage(this, "Please enter content")
            }
            else{
                if (intent !=null && intent.hasExtra(MyKeys.shouldJournal)){
                    viewModel.editJournal(journalList.id, jourTitle, jourContent)
                }else{
                    viewModel.setInsertJournal(jourTitle, jourContent)
                }
            }
        }
    }

    private fun setObserver(viewModel: JournalViewModel) {

        viewModel.successJournal.observe(this){
          Intent(this, JournalListActivity::class.java).also {
              startActivity(it)
          }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }
}