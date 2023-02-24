package com.dialupdelta.ui.get_start_activity

import androidx.lifecycle.MutableLiveData
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.network.response.get_gender_response.AgeGroup
import com.dialupdelta.data.network.response.get_gender_response.GenderList
import com.dialupdelta.data.network.response.intro_video_response.IntroVideo
import com.dialupdelta.data.network.response.ocean_response.OceanData
import com.dialupdelta.data.network.response.summary.SummaryList
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException

class GetStartViewModel(private val repository: Repository): BaseViewModel() {

    private var genderList = MutableLiveData<ArrayList<GenderList>>()
    private var summaryList = MutableLiveData<ArrayList<SummaryList>>()
    var ageList = MutableLiveData<ArrayList<AgeGroup>>()
    var getStartSuccess = MutableLiveData<Boolean>()
    var introVideoData = MutableLiveData<IntroVideo>()
    var getStartVideoLink = MutableLiveData<String>()
    var getOceanData = MutableLiveData<OceanData>()

    init {
        ageList.value = ArrayList()
        genderList.value = ArrayList()
        summaryList.value = ArrayList()
    }

    fun getAgeList(): ArrayList<AgeGroup>? {
        return ageList.value
    }

    fun getGenderList(): ArrayList<GenderList>? {
        return genderList.value
    }

    fun setGender(id: Int?) {
        repository.setGender(id)
    }

    fun setAge(id: Int?) {
        repository.setAge(id)
    }

    fun getSummaryList(): ArrayList<SummaryList>? {
        return summaryList.value
    }

    fun getVideoList():IntroVideo?{
      return introVideoData.value
    }

    fun getAgeGenderApi() {
        startLoading()
        Coroutines.io {
            try {
                val genderResponse = repository.getAgeGenderApi()
                Coroutines.main {
                    stopLoading()
                    if (genderResponse.status) {
                         genderList.value?.addAll(genderResponse.result.genderList)
                         ageList.value?.addAll(genderResponse.result.ageGroup)
                         getStartVideoLink.value = genderResponse.result.infoPageVideo
                         getStartSuccess.value = true
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

    fun getIntroductionVideoApi() {
        startLoading()
        Coroutines.io {
            try {
                val videoResponse = repository.getIntroductionVideoApi()
                Coroutines.main {
                    stopLoading()
                    if (videoResponse.status) {
                       introVideoData.value = videoResponse.result
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

    fun getOceanDataApi(traitName:String) {
        startLoading()
        Coroutines.io {
            try {
                val oceanResponse = repository.getOceanDataApi(traitName)
                Coroutines.main {
                    stopLoading()
                    if (oceanResponse.status) {
                        getOceanData.value = oceanResponse.result
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