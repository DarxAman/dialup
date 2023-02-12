package com.dialupdelta.ui.library

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityLibraryInfoBinding
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.hideStatusBar
import com.dialupdelta.utils.setUserImage

class LibraryInfoActivity : BaseActivity() {
    private lateinit var binding:ActivityLibraryInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_library_info)
        initUI()
        hideStatusBar(this)
    }

    private fun initUI() {
      val listData = intent.getSerializableExtra(MyKeys.libraryList) as LibraryModel

        binding.imageViewVideo.setUserImage(this, listData.thumb)
        binding.circleImageView.setUserImage(this, listData.authorImg)
        binding.videoname.text = listData.nameVideo
        binding.duration.text = "05:30"
        binding.Description.text = listData.description

        binding.playBtn.setOnClickListener {
            val intent = Intent(this, LibraryPlayVideoActivity::class.java)
            intent.putExtra(MyKeys.link, listData.videoLink)
            startActivity(intent)
        }
    }
}