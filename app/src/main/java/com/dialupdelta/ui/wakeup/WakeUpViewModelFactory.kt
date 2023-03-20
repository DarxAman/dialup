package com.dialupdelta.ui.wakeup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dialupdelta.data.repositories.Repository

class WakeUpViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WakeUpViewModel(repository) as T
    }
}

