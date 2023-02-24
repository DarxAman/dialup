package com.dialupdelta.ui.get_to_sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dialupdelta.data.repositories.Repository

class GetToSleepViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetToSleepViewModel(repository) as T
    }
}