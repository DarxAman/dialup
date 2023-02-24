package com.dialupdelta.ui.login_signup

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dialupdelta.base.BaseViewModel
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.ui.discount.CouponAppliedActivity
import com.dialupdelta.ui.discount.DiscountCodeActivity
import com.dialupdelta.utils.ApiException
import com.dialupdelta.utils.Coroutines
import com.dialupdelta.utils.NoInternetException
import com.dialupdelta.utils.showToastMessage

class LoginSignUpViewModel(private val repository: Repository): BaseViewModel() {

    fun signUpApi(context: Context, userName:String, email:String, password:String) {
        startLoading()
        Coroutines.io {
            try {
                val signUpResponse = repository.signUpApi(userName, email, password)
                Coroutines.main {
                    stopLoading()
                    if (signUpResponse.status) {
                        repository.saveAuthData(signUpResponse.result)
                        Intent(context, DiscountCodeActivity::class.java).also {
                            context.startActivity(it)
                        }
                    }
                    else {
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
                    if (loginResponse.status) {
                        repository.saveAuthData(loginResponse.result)
                        Intent(context, DiscountCodeActivity::class.java).also {
                            context.startActivity(it)
                        }
                    }
                    else{
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

    fun sendOtpApi(context: Context) {
        startLoading()
        Coroutines.io {
            try {
                val otpResponse = repository.sendOtpApi()
                Coroutines.main {
                    stopLoading()
                    if (!otpResponse.status) {
                        showToastMessage(context, otpResponse.msg)
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

    fun verifyOtpApi(context: Context, otp:String) {
        startLoading()
        Coroutines.io {
            try {
                val otpResponse = repository.verifyOtpApi(otp)
                Coroutines.main {
                    stopLoading()
                    if (otpResponse.status) {
                        Intent(context, CouponAppliedActivity::class.java).also {
                            context.startActivity(it)
                        }
                    }
                    else{
                        showToastMessage(context, otpResponse.msg)
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