package com.dialupdelta.ui.get_to_sleep

import androidx.lifecycle.MutableLiveData
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.network.response.get_library_response.LibraryModelList
import com.dialupdelta.data.network.response.get_library_response.LibraryResponse
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepList
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepVideoData
import com.dialupdelta.data.network.response.get_to_sleep_response.SaveGetToSleep
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.ProgramList
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException

class GetToSleepViewModel(private val repository: Repository):BaseViewModel() {

    private var getToSleepProgramList = MutableLiveData<ArrayList<GetToSleepList>>()
    private var getToSleepVideoList = MutableLiveData<GetToSleepVideoData>()
    private var getToSleepLibraryList = MutableLiveData<ArrayList<LibraryModelList>>()
    var successLibraryResonse = MutableLiveData<Boolean>()
    var getToSleepResponse = MutableLiveData<Boolean>()
    var getToSleepVideoResponse = MutableLiveData<Boolean>()
    var getSaveSleepResponse = MutableLiveData<SaveGetToSleep>()

    init {
        getToSleepProgramList.value = ArrayList()
        getToSleepLibraryList.value = ArrayList()
    }

    fun getToSleepProgramList():ArrayList<GetToSleepList>? {
      return getToSleepProgramList.value
    }

    fun getToSleepVideoData():GetToSleepVideoData? {
        return getToSleepVideoList.value
    }
    fun getToSleepLibraryData():ArrayList<LibraryModelList>?{
        return getToSleepLibraryList.value
    }

    fun getToSleepList() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getToSleepList()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        getToSleepProgramList.value?.addAll(sleepResponse.result.list)
                        getToSleepResponse.value = true
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


    fun getToSleepVideoList(gender:Int, duration: Int, program: Int?) {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getToSleepVideoList(gender, duration, program)
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        getToSleepVideoList.value = sleepResponse.result
                        getToSleepVideoResponse.value = true
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

    fun getYoutubeDetailsByLink() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getYoutubeDetailsByLink()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                      getToSleepLibraryList.value?.addAll(sleepResponse.result.list)
                        successLibraryResonse.value = true
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

    fun getToSleepSave(genderId:Int?,durationId:Int?, programId:Int?,videoId:Int?) {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getToSleepSave(genderId, durationId, programId, videoId)
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                       // activity login pass
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

    fun saveGetToSleepApi() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.saveGetToSleepApi()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        getSaveSleepResponse.value = sleepResponse.result
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