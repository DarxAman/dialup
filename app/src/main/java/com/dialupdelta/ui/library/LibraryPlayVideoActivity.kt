package com.dialupdelta.ui.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityLibraryPlayVideoBinding
import com.dialupdelta.utils.MyKeys
import com.dialupdelta.utils.hideStatusBar

class LibraryPlayVideoActivity : BaseActivity() {
    private lateinit var binding:ActivityLibraryPlayVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_library_play_video)
        initUI()
        hideStatusBar(this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initUI() {
        val url = intent.getStringExtra(MyKeys.link)
        binding.webView.webViewClient = MyBrowser()
        binding.webView.settings.loadsImagesAutomatically = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        if (url != null) {
            binding.webView.loadUrl(url)
        }
    }

    private class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}