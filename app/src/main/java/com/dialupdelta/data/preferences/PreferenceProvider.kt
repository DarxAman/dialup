package com.dialupdelta.data.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import com.dialupdelta.data.network.response.get_language_response.Data
import com.dialupdelta.data.network.response.login_response.AuthData
import com.google.gson.Gson

private const val baseURL = "baseURL"
private const val language = "language"
private const val gender = "gender"
private const val userCredential = "userCredential"
private const val login = "login"
private const val age = "age"


class PreferenceProvider(context: Context) {
    private var preference: SharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
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
        return preference.getInt(gender, 0)
    }

    fun setLanguage(id: Int) {
        preference.edit()
            .putInt(language, id)
            .apply()
    }

    fun getLanguage():Int {
        return preference.getInt(language, 0)
    }

    fun setAge(id: Int?) {
        if (id != null) {
            preference.edit()
                .putInt(age, id)
                .apply()
        }
    }

    fun getAge():Int {
        return preference.getInt(age, 0)
    }

    fun saveAuthData(profile: AuthData?) {
        preference.edit()
            .putString(userCredential, gson.toJson(profile))
            .apply()
    }

    fun getAuthData(): AuthData? {
        val json: String? = preference.getString(userCredential, null)
        return gson.fromJson(json, AuthData::class.java)
    }

    fun setLogIn(value:Boolean){
      preference.edit()
          .putBoolean(login, value)
          .apply()
    }

    fun getLogIn():Boolean{
        return preference.getBoolean(login, false)
    }

}