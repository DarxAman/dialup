package com.dialupdelta.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dialupdelta.utils.CustomProgressDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

open class BaseFragment: Fragment(), KodeinAware {
    override val kodein by kodein()
    var progress: CustomProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress = CustomProgressDialog(activity as AppCompatActivity)
    }
}