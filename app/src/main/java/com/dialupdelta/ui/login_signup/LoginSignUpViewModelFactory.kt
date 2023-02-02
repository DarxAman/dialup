package com.dialupdelta.ui.login_signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dialupdelta.data.repositories.Repository

class LoginSignUpViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginSignUpViewModel(repository) as T
    }
}