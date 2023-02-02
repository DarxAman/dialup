package com.dialupdelta.ui.anim_login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityAnimLoginBinding
import com.dialupdelta.ui.login_signup.LoginActivity

class AnimLoginActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_anim_login)

        initUI()
    }

    private fun initUI() {
        binding.skipLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // intent.putExtra("nameLogin", nameLogin)
            startActivity(intent)
        }

        playVideoClick()
    }

    private fun playVideoClick() {
        val videoLoginAnim = findViewById<VideoView>(R.id.videoLoginAnim)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoLoginAnim)
        val uri = Uri.parse("android.resource://" + packageName + "/R.raw/" + R.raw.rocket)
        videoLoginAnim.setVideoURI(uri)
        videoLoginAnim.requestFocus()
        videoLoginAnim.start()

        videoLoginAnim.setOnCompletionListener {
            val intent = Intent(this, LoginActivity::class.java)
          //  intent.putExtra("nameLogin", nameLogin)
            startActivity(intent)
        }
    }
}