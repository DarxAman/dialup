package com.dialupdelta.ui.library

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.dialupdelta.R
import com.dialupdelta.`interface`.LibraryItemClickListener
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityLibraryModulesBinding
import com.dialupdelta.ui.get_to_sleep.GetToSleepViewModel
import com.dialupdelta.ui.get_to_sleep.GetToSleepViewModelFactory
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.hideStatusBar
import org.kodein.di.generic.instance

class LibraryModulesActivity : BaseActivity(), LibraryItemClickListener{
    private lateinit var binding: ActivityLibraryModulesBinding
    private val factory: GetToSleepViewModelFactory by instance()
    private lateinit var viewModel: GetToSleepViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_library_modules)
        initUI()
        hideStatusBar(this)
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[GetToSleepViewModel::class.java]
        setObserver(viewModel)
        viewModel.getYoutubeDetailsByLink()
        binding.backLibrary.setOnClickListener{
           finish()
        }
    }

    private fun setObserver(viewModel: GetToSleepViewModel) {

        viewModel.successLibraryResonse.observe(this) {
            val adapter = LibraryAdapter(this, this, viewModel.getToSleepLibraryData())
            binding.recyclerViewLibrary.layoutManager  = GridLayoutManager(this, 2)
            binding.recyclerViewLibrary.adapter = adapter
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }

    override fun setOnLibraryItemClick(position: Int) {
      Intent(this, LibraryInfoActivity::class.java).also {
          it.putExtra(MyKeys.position, position)
          it.putExtra(MyKeys.libraryList, viewModel.getToSleepLibraryData()?.get(position))
          startActivity(it)
      }
    }
}