package com.dialupdelta.ui.sleep_enhancer

import androidx.lifecycle.MutableLiveData
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.network.response.intro_video_response.IntroVideo
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.AudioDataList
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.ProgramList
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException

class SleepEnhancerViewModel(private val repository: Repository):BaseViewModel(){

    val successProgramList = MutableLiveData<Boolean>()
    private var getProgramList = MutableLiveData<ArrayList<ProgramList>>()
    private var getProgramAudioList = MutableLiveData<ArrayList<ProgramList>>()
    var audioDataList = MutableLiveData<AudioDataList>()
    var noDataFound = MutableLiveData<Boolean>()


    init {
        getProgramList.value = ArrayList()
    }

    fun getSleepProgramList(): ArrayList<ProgramList>? {
        return getProgramList.value
    }

    fun getSleepAudioList():AudioDataList?{
        return audioDataList.value
    }

    fun getSleepEnhancerProgramList() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getSleepEnhancerProgramList()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        getProgramList.value?.addAll(sleepResponse.result.list)
                        successProgramList.value = true
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

    fun getSleepEnhancerDialogList(program:Int?, duration:Int) {
        getSleepAudioList()?.list?.clear()
        Coroutines.io {
            try {
                val sleepResponse = repository.getSleepEnhancerDialogList(program, duration)
                Coroutines.main {
                    if (sleepResponse.status) {
                        audioDataList.value = sleepResponse.result
                    }
                    else{
                        noDataFound.value = true
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

    fun sleepEnhancerSaver(durationId:Int?, programId:Int?,audioId:Int?) {
        Coroutines.io {
            try {
                val sleepResponse = repository.sleepEnhancerSaver(durationId, programId, audioId)
                Coroutines.main {
                    if (sleepResponse.status) {
                       // api success logic code
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

    fun savedSleepEnhancer() {
        Coroutines.io {
            try {
                val sleepResponse = repository.savedSleepEnhancer()
                Coroutines.main {
                    if (sleepResponse.status) {
                        // api success logic code
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


    fun setSleepEnhancerUrl(enhancerUrl: String) {
        repository.setSleepEnhancerUrl(enhancerUrl)
    }

    private fun getSleepEnhancerUrl(): String?{
        return repository.getSleepEnhancerUrl()
    }
}