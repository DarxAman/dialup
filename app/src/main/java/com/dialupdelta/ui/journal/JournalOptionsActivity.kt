package com.dialupdelta.ui.journal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityJournalOptionsBinding

class JournalOptionsActivity : BaseActivity() {
    private lateinit var binding:ActivityJournalOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_options)

        initUI()
    }

    private fun initUI() {
        binding.ivBackJo.setOnClickListener {
            finish()
        }

        binding.clAddEntries.setOnClickListener {
            startActivity(Intent(this, AddJournalActivity::class.java))

        }

        binding.clViewEntries.setOnClickListener {
            startActivity(Intent(this, JournalListActivity::class.java))
        }
    }
}