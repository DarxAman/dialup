package com.dialupdelta.ui.discount

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityPermissionBinding
import com.dialupdelta.ui.transition.TransitionActivity

class PermissionActivity : BaseActivity() {
    private lateinit var binding:ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_permission)

        initUI()
    }

    private fun initUI() {

        binding.btnGrant.setOnClickListener {
          //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                startActivity(Intent(this, TransitionActivity::class.java))
          //  }
        }
    }
}