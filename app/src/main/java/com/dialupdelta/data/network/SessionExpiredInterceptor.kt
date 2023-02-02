package com.dialupdelta.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class SessionExpiredInterceptor(context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
//        if (response.code() == 401) {
//            throw SessionExpiredException(applicationContext.getString(R.string.session_expired))
//        }
        return response
    }
}