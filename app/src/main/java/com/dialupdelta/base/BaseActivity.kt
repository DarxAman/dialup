package com.dialupdelta.base

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.dialupdelta.R
import com.dialupdelta.utils.CustomProgressDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

open class BaseActivity: AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    var progress: CustomProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress = CustomProgressDialog(this)
    }
}