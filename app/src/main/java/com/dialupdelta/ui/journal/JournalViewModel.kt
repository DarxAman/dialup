package com.dialupdelta.ui.journal

import androidx.lifecycle.MutableLiveData
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.network.response.get_journal_response.JournalList
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException

class JournalViewModel(private val repository: Repository):BaseViewModel() {

    private val getJournalList = MutableLiveData<ArrayList<JournalList>>()
    val successJournalList = MutableLiveData<Boolean>()

    init {
        getJournalList.value = ArrayList()
    }

   fun getAllJournalList():ArrayList<JournalList>?{
       return getJournalList.value
   }


    fun getJournalList() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getJournalList()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
                        getJournalList.value?.addAll(sleepResponse.result)
                        successJournalList.value = true
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

    fun getSingleJournalList() {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.getSingleJournalList()
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
//                        getProgramList.value = sleepResponse.result
//                        successProgramList.value = true
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

    fun setInsertJournal(title:String, content:String) {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.setInsertJournal(title, content)
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
//                        getProgramList.value = sleepResponse.result
//                        successProgramList.value = true
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

    fun editJournal(id: Int, title:String, content:String) {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.editJournal(id, title, content)
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
//                        getProgramList.value = sleepResponse.result
//                        successProgramList.value = true
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

    fun deleteJournal(id: Int) {
        startLoading()
        Coroutines.io {
            try {
                val sleepResponse = repository.deleteJournal(id)
                Coroutines.main {
                    stopLoading()
                    if (sleepResponse.status) {
//                        getProgramList.value = sleepResponse.result
//                        successProgramList.value = true
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