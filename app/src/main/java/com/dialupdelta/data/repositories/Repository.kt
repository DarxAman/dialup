package com.dialupdelta.data.repositories

import com.dialupdelta.data.network.MyApi
import com.dialupdelta.data.network.SafeApiRequest
import com.dialupdelta.data.network.response.SignUpResponse
import com.dialupdelta.data.network.response.get_gender_response.AgeGenderResponse
import com.dialupdelta.data.network.response.get_language_response.LanguageResponse
import com.dialupdelta.data.network.response.intro_video_response.IntroVideoResponse
import com.dialupdelta.data.network.response.login_response.AuthData
import com.dialupdelta.data.network.response.login_response.LoginResponse
import com.dialupdelta.data.network.response.summary.GetSummaryResponse
import com.dialupdelta.data.preferences.PreferenceProvider

class Repository(
    private val api: MyApi,
    private val prefs: PreferenceProvider
): SafeApiRequest() {

    private fun getBaseURL(): String? {
       return prefs.getBaseURL()
    }

    fun setGender(id: Int?) {
       prefs.setGender(id)
    }

    private fun getGender(): Int {
        return prefs.getGender()
    }

    fun setLanguage(id: Int) {
        prefs.setLanguage(id)
    }

    private fun getLanguage(): Int {
        return prefs.getLanguage()
    }

    fun saveAuthData(authData: AuthData?) {
        prefs.saveAuthData(authData)
        prefs.setLogIn(true)
        authData?.languageId?.let { setLanguage(it) }
        setGender(authData?.genderId)
    }

    private fun getAuthData(): AuthData? {
        return prefs.getAuthData()
    }

    fun getLogin():Boolean{
        return prefs.getLogIn()
    }

    suspend fun getLanguageApi(): LanguageResponse {
        return apiRequest {
            api.getLanguageApi(
                getBaseURL()
            )
        }
    }

    suspend fun getAgeGenderApi(): AgeGenderResponse {
        return apiRequest {
            api.getAgeGenderApi(
                getBaseURL()
            )
        }
    }

    suspend fun signUpApi(userName:String, email:String, password:String): SignUpResponse {
        return apiRequest {
            api.signUpApi(
                getBaseURL(),
                userName,
                email,
                password,
                getGender(),
                getLanguage()
            )
        }
    }

    suspend fun loginApi(email:String, password:String): LoginResponse {
        return apiRequest {
            api.loginApi(
                getBaseURL(),
                email,
                password
            )
        }
    }

    suspend fun apiLowHigh():GetSummaryResponse{
        return apiRequest {
            api.apiLowHigh(
               "https://app.whyuru.com/api/question/summary"
            )
        }
    }

    suspend fun getIntroductionVideoApi(): IntroVideoResponse {
        return apiRequest {
            api.getIntroductionVideoApi(
                getBaseURL()
            )
        }
    }
}