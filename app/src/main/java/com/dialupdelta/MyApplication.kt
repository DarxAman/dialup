package com.dialupdelta

import android.app.Application
import android.content.Context
import com.dialupdelta.data.network.MyApi
import com.dialupdelta.data.network.NetworkConnectionInterceptor
import com.dialupdelta.data.network.SessionExpiredInterceptor
import com.dialupdelta.data.preferences.PreferenceProvider
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.ui.get_start_activity.GetStartViewModelFactory
import com.dialupdelta.ui.get_to_sleep.GetToSleepViewModelFactory
import com.dialupdelta.ui.login_signup.LoginSignUpViewModelFactory
import com.dialupdelta.ui.sleep_enhancer.SleepEnhancerViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { SessionExpiredInterceptor(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { MyApi(instance(), instance()) }
        bind() from singleton { Repository(instance(), instance()) }
        bind() from provider { GetStartViewModelFactory(instance()) }
        bind() from provider { LoginSignUpViewModelFactory(instance()) }
        bind() from provider { GetToSleepViewModelFactory(instance()) }
        bind() from provider { SleepEnhancerViewModelFactory(instance()) }
    }

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun getApplicationContext(): Context? {
            return instance?.applicationContext
        }
    }
}