package com.dialupdelta.ui.journal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivitySurveyBinding

class SurveyActivity : BaseActivity() {
    private lateinit var binding:ActivitySurveyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_survey)
        initUI()
    }

    private fun initUI() {

    }
}