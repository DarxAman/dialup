package com.dialupdelta.ui.discount

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityCouponAppliedBinding

class CouponAppliedActivity : BaseActivity() {
    private lateinit var binding:ActivityCouponAppliedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_coupon_applied)
        initUI()
    }

    private fun initUI() {

        binding.monthlyGo.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                startActivity(Intent(this, PermissionActivity::class.java))
            }
//            else {
//                startActivity(Intent(this, TransitionActivity::class.java))
//            }
        }

        binding.yearlyGo.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                startActivity(Intent(this, PermissionActivity::class.java))
            }
//            else {
//                startActivity(Intent(this, TransitionActivity::class.java))
//            }
        }
    }
}