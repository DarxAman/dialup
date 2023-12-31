package com.dialupdelta.data.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import com.dialupdelta.data.network.response.login_response.AuthData
import com.dialupdelta.ui.sleep_enhancer.LocalSaveSleepEnhancer
import com.google.gson.Gson

private const val baseURL = "baseURL"
private const val language = "language"
private const val gender = "gender"
private const val userCredential = "userCredential"
private const val login = "login"
private const val age = "age"
private const val sleepEnhancerUrl = "sleepEnhancerUrl"
private const val wakeUpVideoData = "wakeUpVideoData"
private const val wakeUpThumbData = "wakeUpThumbData"
private const val sleepEnhancerData = "sleepEnhancerData"


class PreferenceProvider(context: Context) {
    private var preference: SharedPreferences =
        context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    private val gson = Gson()

    fun getBaseURL(): String? {
        return preference.getString(baseURL, "https://dialupdelta.com/web/api/")
    }

    fun setGender(id: Int?) {
        if (id != null) {
            preference.edit()
                .putInt(gender, id)
                .apply()
        }
    }

    fun getGender(): Int {
        return preference.getInt(gender, 1)
    }

    fun setLanguage(id: Int) {
        preference.edit()
            .putInt(language, id)
            .apply()
    }

    fun getLanguage(): Int {
        return preference.getInt(language, 1)
    }

    fun setAge(id: Int?) {
        if (id != null) {
            preference.edit()
                .putInt(age, id)
                .apply()
        }
    }

    fun getAge(): Int {
        return preference.getInt(age, 1)
    }

    fun saveAuthData(profile: AuthData?) {
        preference.edit()
            .putString(userCredential, gson.toJson(profile))
            .apply()
    }

    fun clearData() {
        val newPref = preference.edit()
        newPref.clear()
        newPref.putString(userCredential, "")
        newPref.putString(login, "")
        newPref.apply()

    }

    fun getAuthData(): AuthData? {
        val json: String? = preference.getString(userCredential, null)
        return gson.fromJson(json, AuthData::class.java)
    }

    fun setLogIn(value: Boolean) {
        preference.edit()
            .putBoolean(login, value)
            .apply()
    }

    fun getLogIn(): Boolean {
        return preference.getBoolean(login, false)
    }


    fun setSleepEnhancerUrl(enhancerUrl: String) {
        preference.edit()
            .putString(sleepEnhancerUrl, enhancerUrl)
            .apply()
    }

    fun getSleepEnhancerUrl(): String? {
        return preference.getString(sleepEnhancerUrl, "")
    }

    fun setWakeUpVideoData(localData: String) {
        preference.edit()
            .putString(wakeUpVideoData, localData)
            .apply()
    }

    fun getWakeUpVideoData(): String? {
        return preference.getString(wakeUpVideoData, "")
    }


    fun setWakeUpThumbData(localData: String) {
        preference.edit()
            .putString(wakeUpThumbData, localData)
            .apply()
    }

    fun getWakeUpThumbData(): String? {
        return preference.getString(wakeUpThumbData, "")
    }

    fun saveSleepEnhancerData(localSaveSleepEnhancer: LocalSaveSleepEnhancer?) {
        preference.edit()
            .putString(sleepEnhancerData, gson.toJson(localSaveSleepEnhancer))
            .apply()
    }

    fun getSleepEnhancerData(): LocalSaveSleepEnhancer? {
        val json: String? = preference.getString(sleepEnhancerData, null)
        return gson.fromJson(json, LocalSaveSleepEnhancer::class.java)
    }

}