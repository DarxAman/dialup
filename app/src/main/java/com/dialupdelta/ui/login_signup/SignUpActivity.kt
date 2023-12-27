package com.dialupdelta.ui.login_signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivitySignUpBinding
import com.dialupdelta.ui.discount.DiscountCodeActivity
import org.kodein.di.generic.instance

class SignUpActivity : BaseActivity() {
    private val factory: LoginSignUpViewModelFactory by instance()
    private lateinit var viewModel: LoginSignUpViewModel
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[LoginSignUpViewModel::class.java]
        setObserver(viewModel)

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            val userName = binding.edtName.text.toString()
            val userEmail = binding.edtEmail.text.toString()
            val userPassword = binding.edtPassword.text.toString()
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (userEmail.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else if (userPassword.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.signUpApi(this, userName, userEmail, userPassword)
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