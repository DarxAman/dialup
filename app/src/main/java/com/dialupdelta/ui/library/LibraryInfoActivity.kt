package com.dialupdelta.ui.library

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.data.network.response.get_library_response.LibraryModelList
import com.dialupdelta.databinding.ActivityLibraryInfoBinding
import com.dialupdelta.ui.get_to_sleep.GetToSleepViewModel
import com.dialupdelta.ui.get_to_sleep.GetToSleepViewModelFactory
import com.dialupdelta.ui.sleep_enhancer.SleepEnhancerViewModel
import com.dialupdelta.ui.sleep_enhancer.SleepEnhancerViewModelFactory
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setUserImage
import org.kodein.di.generic.instance

class LibraryInfoActivity : BaseActivity() {
    private val factory: GetToSleepViewModelFactory by instance()
    private lateinit var viewModel: GetToSleepViewModel
    private lateinit var binding:ActivityLibraryInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_library_info)
        initUI()
        hideStatusBar(this)
    }
    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[GetToSleepViewModel::class.java]

        binding.backLibraryInfo.setOnClickListener {
            finish()
        }

      val listData = intent.getSerializableExtra(MyKeys.libraryList) as LibraryModelList
        binding.imageViewVideo.setUserImage(this, listData.thumbnail_url)
        binding.circleImageView.setUserImage(this, listData.author_url)
        binding.videoname.text = listData.mainlink
        binding.duration.text = "05:30"
        binding.Description.text = listData.title

        binding.playBtn.setOnClickListener {
            val intent = Intent(this, LibraryPlayVideoActivity::class.java)
            intent.putExtra(MyKeys.link, listData.mainlink)
            startActivity(intent)
        }
    }
}