package com.dialupdelta.ui.library

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.`interface`.LibraryItemClickListener
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityLibraryModulesBinding
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.hideStatusBar

class LibraryModulesActivity : BaseActivity(), LibraryItemClickListener{
    private lateinit var binding:ActivityLibraryModulesBinding
    private val list: List<LibraryModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_library_modules)
        initUI()
        hideStatusBar(this)
    }

    private fun initUI() {
        binding.backLibrary.setOnClickListener{
           finish()
        }
    }

    override fun setOnLibraryItemClick(position: Int, list: LibraryModel) {
      Intent(this, LibraryInfoActivity::class.java).also {
          it.putExtra(MyKeys.position, position)
          it.putExtra(MyKeys.libraryList, list)
          startActivity(it)
      }
    }
}