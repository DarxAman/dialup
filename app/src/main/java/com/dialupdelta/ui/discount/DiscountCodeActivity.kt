package com.dialupdelta.ui.discount


import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityDiscountCodeBinding
import com.dialupdelta.ui.login_signup.LoginSignUpViewModel
import com.dialupdelta.ui.login_signup.LoginSignUpViewModelFactory
import com.dialupdelta.utils.showToastMessage
import org.kodein.di.generic.instance

class DiscountCodeActivity : BaseActivity() {
    private val factory: LoginSignUpViewModelFactory by instance()
    private lateinit var viewModel: LoginSignUpViewModel
    private lateinit var binding:ActivityDiscountCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discount_code)

        initUI()

        binding.skipBtnDiscount.setOnClickListener({
            startActivity(Intent(this, PermissionActivity::class.java))
        })
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[LoginSignUpViewModel::class.java]
        setObserver(viewModel)
        viewModel.sendOtpApi(this)
       binding.buttonSubmitCoupon.setOnClickListener {

           if (binding.enterOtp.text.isEmpty()){
               showToastMessage(this, "Please enter otp")
           }
           else if (binding.enterOtp.text.length < 4){
               showToastMessage(this, "Please enter valid  otp")
           }
           else{
               viewModel.verifyOtpApi(this, binding.enterOtp.text.toString())
           }

       }
    }

    private fun setObserver(viewModel: LoginSignUpViewModel) {

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }
}