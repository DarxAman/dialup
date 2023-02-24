package com.dialupdelta.ui.sleep_enhancer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dialupdelta.data.repositories.Repository


class SleepEnhancerViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SleepEnhancerViewModel(repository) as T
    }
}