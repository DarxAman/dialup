package com.dialupdelta.ui.journal


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dialupdelta.R
import com.dialupdelta.`interface`.JournalEventListener
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.databinding.ActivityJournalListBinding
import com.dialupdelta.utils.MyKeys
import org.kodein.di.generic.instance

class JournalListActivity : BaseActivity(), JournalEventListener {
    private lateinit var binding:ActivityJournalListBinding
    private val factory: JournalViewModelFactory by instance()
    private lateinit var viewModel: JournalViewModel
    private lateinit var journalsListAdapter: JournalsListAdapter
    private val repository: Repository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_list)

        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[JournalViewModel::class.java]
        viewModel.getJournalList()
        setObserver(viewModel)

        binding.addJournal.setOnClickListener {
            val intent = Intent(this, AddJournalActivity::class.java)
            startActivity(intent)
        }

        binding.nameJornal.text = (repository.getAuthData()?.name?.split(" ")?.get(0) ?: "User") +"'s Journal"

        binding.ivSearch.setOnClickListener {
            journalsListAdapter.filter(binding.searchEdit.text.toString())
        }

        binding.print.setOnClickListener {
            showChooserDialog()
        }

        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                journalsListAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun setObserver(viewModel: JournalViewModel) {

        viewModel.successJournalList.observe(this){
            Log.e("journal : ", viewModel.getAllJournalList().toString())
             journalsListAdapter = JournalsListAdapter(this, viewModel.getAllJournalList())
            binding.recyclerViewJournals.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewJournals.setHasFixedSize(true)
            binding.recyclerViewJournals.adapter = journalsListAdapter

            binding.dateJournl.text = "Last Updated on : "+viewModel.getAllJournalList()?.get(0)?.updated_at
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

    private fun showChooserDialog() {
        val options = arrayOf("Gmail", "WhatsApp")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an App")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> openGmail()
                    1 -> openWhatsApp()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun openGmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("recipient@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, viewModel.getJournalListResponse())
        emailIntent.putExtra(Intent.EXTRA_TEXT, viewModel.getAllJournalList())
        startActivity(emailIntent)

//        Log.e("em : ", emailIntent.toString());
    }

    private fun openWhatsApp() {
        val whatsappIntent = Intent(Intent.ACTION_VIEW)
        whatsappIntent.data = Uri.parse("https://wa.me/1234567890/?text=${viewModel.getJournalListResponse()}%20Journals-%20${viewModel.getAllJournalList()}")
        startActivity(whatsappIntent)
    }
}