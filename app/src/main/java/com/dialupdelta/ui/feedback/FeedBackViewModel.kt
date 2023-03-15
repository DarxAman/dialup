package com.dialupdelta.ui.feedback

import androidx.lifecycle.MutableLiveData
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.network.response.feedback_response.AllFeedBackList
import com.dialupdelta.data.network.response.feedback_response.FeedBackDetails
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepList
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException

class FeedBackViewModel(private val repository: Repository): BaseViewModel() {

    private var feedBackDetails = MutableLiveData<ArrayList<FeedBackDetails>>()
    private var allFeedBackDetails = MutableLiveData<ArrayList<AllFeedBackList>>()
    var feedBackDetailsResponse = MutableLiveData<Boolean>()

    init {
        feedBackDetails.value = ArrayList()
        allFeedBackDetails.value = ArrayList()
    }

    fun getFeedBackDetails():ArrayList<FeedBackDetails>? {
        return feedBackDetails.value
    }

    fun getAllFeedBackList():ArrayList<AllFeedBackList>? {
        return allFeedBackDetails.value
    }

    fun feedbackDetails() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.feedbackDetails()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        feedBackDetails.value?.addAll(sleepResponse.result.list)
                        feedBackDetailsResponse.value = true
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

    fun allFeedbackDetails() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.allFeedbackDetails()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        allFeedBackDetails.value?.addAll(sleepResponse.result)
                        feedBackDetailsResponse.value = true
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