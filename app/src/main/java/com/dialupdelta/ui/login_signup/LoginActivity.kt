package com.dialupdelta.ui.login_signup


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityLoginBinding
import com.dialupdelta.ui.discount.DiscountCodeActivity
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity() {
    private val factory: LoginSignUpViewModelFactory by instance()
    private lateinit var viewModel: LoginSignUpViewModel
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        initUI()
    }

    private fun initUI() {

        viewModel = ViewModelProvider(this, factory)[LoginSignUpViewModel::class.java]
        setObserver(viewModel)


        binding.btnProceed.setOnClickListener {
            val userEmail = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            } else {
                Intent(this, DiscountCodeActivity::class.java).also {
                    startActivity(it)
                }
               // viewModel.loginApi(this, userEmail, password)
            }
        }

        binding.tvSignUp.setOnClickListener {
            Intent(this, SignUpActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setObserver(viewModel: LoginSignUpViewModel) {

//        viewModel.successLogin.observe(this) {
//            Intent(this, DiscountCodeActivity::class.java).also {
//                startActivity(it)
//            }
//        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }
}