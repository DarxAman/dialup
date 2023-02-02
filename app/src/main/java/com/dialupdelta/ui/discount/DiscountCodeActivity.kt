package com.dialupdelta.ui.discount


import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityDiscountCodeBinding

class DiscountCodeActivity : BaseActivity() {
    private lateinit var binding:ActivityDiscountCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discount_code)

        initUI()
    }

    private fun initUI() {
       binding.buttonSubmitCoupon.setOnClickListener {
           Intent(this, CouponAppliedActivity::class.java).also {
               startActivity(it)
           }
       }
    }
}