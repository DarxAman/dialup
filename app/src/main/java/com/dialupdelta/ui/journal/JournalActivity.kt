package com.dialupdelta.ui.journal

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityJournalBinding
import com.dialupdelta.databinding.ActivitySurveyBinding

class JournalActivity : BaseActivity() {
    private lateinit var binding: ActivityJournalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_journal)

        initUI()
    }

    private fun initUI() {

        binding.btnJournalContinue.setOnClickListener{
            Intent(this, JournalOptionsActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}