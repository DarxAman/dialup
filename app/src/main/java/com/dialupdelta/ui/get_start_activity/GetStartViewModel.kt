package com.dialupdelta.ui.get_start_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.network.response.get_age_response.AgeData
import com.dialupdelta.data.network.response.get_gender_response.Gender
import com.dialupdelta.data.network.response.summary.SummaryList
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException

class GetStartViewModel(private val repository: Repository): BaseViewModel() {

    private var genderList = MutableLiveData<ArrayList<Gender>>()
    private var summaryList = MutableLiveData<ArrayList<SummaryList>>()
    var ageList = MutableLiveData<ArrayList<AgeData>>()
    var ageSuccess = MutableLiveData<Boolean>()
    var genderSuccess = MutableLiveData<Boolean>()
    var successSummaryList = MutableLiveData<Boolean>()

    init {
        ageList.value = ArrayList()
        genderList.value = ArrayList()
        summaryList.value = ArrayList()
    }

    fun getAgeList(): ArrayList<AgeData>? {
        return ageList.value
    }

    fun getGenderList(): ArrayList<Gender>? {
        return genderList.value
    }

    fun setGender(id: Int?) {
        repository.setGender(id)
    }

    fun getSummaryList(): ArrayList<SummaryList>? {
        return summaryList.value
    }

    fun getGenderApi() {
        startLoading()
        Coroutines.io {
            try {
                val genderResponse = repository.getGenderApi()
                Coroutines.main {
                    stopLoading()
                    if (genderResponse.status == 1) {
                         genderList.value?.addAll(genderResponse.data)
                         genderSuccess.value = true
                    }
                    return@main
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            } catch (e: NoInternetException) {
                e.printStackTrace()
            }
        }
    }

    fun getAgeApi() {
        startLoading()
        Coroutines.io {
            try {
                val ageResponse = repository.getAgeApi()
                Coroutines.main {
                    stopLoading()
                    if (ageResponse.status == 1) {
                        ageList.value?.addAll(ageResponse.data)
                        ageSuccess.value = true
                    }
                    return@main
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            } catch (e: NoInternetException) {
                e.printStackTrace()
            }
        }
    }

    fun apiLowHigh() {
        startLoading()
        Coroutines.io {
            try {
                val summaryResponse = repository.apiLowHigh()
                Coroutines.main {
                    stopLoading()
                    if (summaryResponse.valid && summaryResponse.data.isNotEmpty()) {
                       summaryList.value?.addAll(summaryResponse.data)
                        successSummaryList.value = true
                    }
                    return@main
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            } catch (e: NoInternetException) {
                e.printStackTrace()
            }
        }
    }

}