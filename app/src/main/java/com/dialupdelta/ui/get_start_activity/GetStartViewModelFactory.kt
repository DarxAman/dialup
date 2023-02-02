package com.dialupdelta.ui.get_start_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dialupdelta.data.repositories.Repository

class GetStartViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetStartViewModel(repository) as T
    }
}