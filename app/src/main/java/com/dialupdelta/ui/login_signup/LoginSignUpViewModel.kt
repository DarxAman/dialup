package com.dialupdelta.ui.login_signup

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException
import com.dialupdelta.utils.showToastMessage

class LoginSignUpViewModel(private val repository: Repository): BaseViewModel() {

    val successSignUp = MutableLiveData<Boolean>()
    val successLogin = MutableLiveData<Boolean>()

    fun signUpApi(context: Context, userName:String, email:String, password:String) {
        startLoading()
        Coroutines.io {
            try {
                val signUpResponse = repository.signUpApi(userName, email, password)
                Coroutines.main {
                    stopLoading()
                    if (signUpResponse.status == 1) {
                      successSignUp.value = true
                    }
                    else if(signUpResponse.status == 0 ) {
                      showToastMessage(context, signUpResponse.msg)
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

    fun loginApi(context:Context, email:String, password:String) {
        startLoading()
        Coroutines.io {
            try {
                val loginResponse = repository.loginApi(email, password)
                Coroutines.main {
                    stopLoading()
                    if (loginResponse.status == 1) {
                        repository.saveAuthData(loginResponse.data)
                        successLogin.value = true
                    }
                    else if (loginResponse.status == 0){
                        showToastMessage(context, loginResponse.msg)
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