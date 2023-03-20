package com.dialupdelta.ui.wakeup

import androidx.lifecycle.MutableLiveData
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.network.response.wake_up_response.WakeUp
import com.dialupdelta.data.network.response.wake_up_response.WakeUpProgramList
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException

class WakeUpViewModel(private val repository: Repository): BaseViewModel() {

    private val wakeUpProgramList = MutableLiveData<ArrayList<WakeUpProgramList>>()
    val successWakeUpProgram = MutableLiveData<Boolean>()
    val successWakeUp = MutableLiveData<Boolean>()
    val wakeUpProgramResponse = MutableLiveData<WakeUp>()

    init {
        wakeUpProgramList.value = ArrayList()
    }

    fun getWakeUpProgramList():ArrayList<WakeUpProgramList>?{
        return wakeUpProgramList.value
    }

    fun allWakeVideoList(): WakeUp?{
        return wakeUpProgramResponse.value
    }


    fun getWakeProgramList() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getWakeUpProgramList()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        sleepResponse.result?.list?.let { wakeUpProgramList.value?.addAll(it) }
                        successWakeUpProgram.value = true
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

    fun getWakeList(gender:Int, program:Int?) {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getWakeUpList(gender, program)
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        wakeUpProgramResponse.value = sleepResponse.result
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

    fun fetchWakeUpSaved() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.fetchWakeUpSaved()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        // all login code
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

    fun wakeUpSaver(gender:String, program:String, thumbUrl:String, videoUrl:String) {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.wakeUpSaver(gender, program, thumbUrl, videoUrl)
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        // all login code
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