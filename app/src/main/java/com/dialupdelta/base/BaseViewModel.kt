package com.dialupdelta.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel:ViewModel() {
    var isLoading = MutableLiveData<Boolean>()

    fun startLoading() {
        stopLoading()
        isLoading.value = true
    }

    fun stopLoading() {
        isLoading.value = false
    }
}
