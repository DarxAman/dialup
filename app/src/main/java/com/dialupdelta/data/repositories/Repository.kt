package com.dialupdelta.data.repositories

import com.dialupdelta.data.network.MyApi
import com.dialupdelta.data.network.SafeApiRequest
import com.dialupdelta.data.network.response.feedback_response.AllFeedBackListResponse
import com.dialupdelta.data.network.response.feedback_response.FeedBackDetailsResponse
import com.dialupdelta.data.network.response.get_gender_response.AgeGenderResponse
import com.dialupdelta.data.network.response.get_journal_response.DeleteJournal
import com.dialupdelta.data.network.response.get_journal_response.DeleteJournalResponse
import com.dialupdelta.data.network.response.get_journal_response.GetJournalList
import com.dialupdelta.data.network.response.get_journal_response.GetSingleJournalList
import com.dialupdelta.data.network.response.get_language_response.LanguageResponse
import com.dialupdelta.data.network.response.get_library_response.LibraryResponse
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepList
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepListResponse
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepResponse
import com.dialupdelta.data.network.response.get_to_sleep_response.SaveGetToSleepResponse
import com.dialupdelta.data.network.response.intro_video_response.IntroVideoResponse
import com.dialupdelta.data.network.response.login_response.AuthData
import com.dialupdelta.data.network.response.login_response.SignUpLoginResponse
import com.dialupdelta.data.network.response.ocean_response.OceanResponse
import com.dialupdelta.data.network.response.otp_response.OtpResponse
import com.dialupdelta.data.network.response.sipmle_response.SimpleResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SavedSleepEnhancer
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SavedSleepEnhancerResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SleepEnhancerProgramListResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SleepEnhancerResponse
import com.dialupdelta.data.network.response.summary.GetSummaryResponse
import com.dialupdelta.data.preferences.PreferenceProvider
import java.util.*

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

    fun setAge(id: Int?) {
        prefs.setAge(id)
    }

    private fun getAge(): Int {
        return prefs.getAge()
    }

    fun setSleepEnhancerUrl(enhancerUrl: String) {
        prefs.setSleepEnhancerUrl(enhancerUrl)
    }

    fun getSleepEnhancerUrl(): String?{
        return prefs.getSleepEnhancerUrl()
    }


    fun saveAuthData(authData: AuthData?) {
        prefs.saveAuthData(authData)
        prefs.setLogIn(true)
        authData?.language_id?.let { setLanguage(it) }
        setGender(authData?.gender_id)
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

    suspend fun signUpApi(userName:String, email:String, password:String): SignUpLoginResponse {
        return apiRequest {
            api.signUpApi(
                getBaseURL(),
                userName,
                email,
                password,
                getGender(),
                getLanguage(),
                getAge()
            )
        }
    }

    suspend fun loginApi(email:String, password:String): SignUpLoginResponse {
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

    suspend fun getOceanDataApi(traitName:String): OceanResponse {
        return apiRequest {
            api.getOceanDataApi(
                getBaseURL(),
                traitName
            )
        }
    }

    suspend fun sendOtpApi(): OtpResponse {
        return apiRequest {
            api.sendOtpApi(
                getBaseURL(),
                getAuthData()?.id!!
            )
        }
    }

    suspend fun verifyOtpApi(otp:String): OtpResponse {
        return apiRequest {
            api.verifyOtpApi(
                getBaseURL(),
                getAuthData()?.id!!,
                otp
            )
        }
    }

    suspend fun getSleepEnhancerProgramList(): SleepEnhancerProgramListResponse {
        return apiRequest {
            api.getSleepEnhancerProgramList(
                getBaseURL()
            )
        }
    }

    suspend fun getSleepEnhancerDialogList(program:Int?, duration:Int): SleepEnhancerResponse {
        return apiRequest {
            api.getSleepEnhancerDialogList(
                getBaseURL(),
                program,
                duration
            )
        }
    }

    suspend fun getToSleepList(): GetToSleepResponse {
        return apiRequest {
            api.getToSleepList(
                getBaseURL()
            )
        }
    }

    suspend fun getToSleepVideoList(gender:Int, duration: Int, program: Int?): GetToSleepListResponse {
        return apiRequest {
            api.getToSleepVideoList(
                getBaseURL(),
                gender,
                duration,
                program
            )
        }
    }

    suspend fun getJournalList(): GetJournalList {
        return apiRequest {
            api.getJournalList(
                getBaseURL(),
                getAuthData()?.id,
            )
        }
    }

    suspend fun getSingleJournalList(): GetSingleJournalList {
        return apiRequest {
            api.getSingleJournalList(
                getBaseURL(),
                getAuthData()?.id,
            )
        }
    }

    suspend fun setInsertJournal(title:String, content:String): GetSingleJournalList {
        return apiRequest {
            api.setInsertJournal(
                getBaseURL(),
                getAuthData()?.id,
                title,
                content
            )
        }
    }

    suspend fun editJournal(id: Int, title:String, content:String): GetSingleJournalList {
        return apiRequest {
            api.editJournal(
                getBaseURL(),
                id,
                title,
                content
            )
        }
    }

    suspend fun deleteJournal(id: Int?): DeleteJournalResponse {
        return apiRequest {
            api.deleteJournal(
                getBaseURL(),
                id
            )
        }
    }

    suspend fun getYoutubeDetailsByLink(): LibraryResponse {
        return apiRequest {
            api.getYoutubeDetailsByLink(
                getBaseURL()
            )
        }
    }

    suspend fun getToSleepSave(genderId:Int?,durationId:Int?, programId:Int?,videoId:Int?): SimpleResponse {
        return apiRequest {
            api.getToSleepSave(
                getBaseURL(),
                getAuthData()?.id,
                genderId,
                durationId,
                programId,
                videoId
            )
        }
    }

    suspend fun saveGetToSleepApi(): SaveGetToSleepResponse {
        return apiRequest {
            api.saveGetToSleepApi(
                getBaseURL(),
                getAuthData()?.id,
            )
        }
    }
    suspend fun savedSleepEnhancer(): SavedSleepEnhancerResponse {
        return apiRequest {
            api.savedSleepEnhancer(
                getBaseURL(),
                getAuthData()?.id,
            )
        }
    }

    suspend fun sleepEnhancerSaver(durationId:Int?, programId:Int?,audioId:Int?): SimpleResponse {
        return apiRequest {
            api.sleepEnhancerSaver(
                getBaseURL(),
                getAuthData()?.id,
                durationId,
                programId,
                audioId
            )
        }
    }

    suspend fun feedbackDetails(): FeedBackDetailsResponse {
        return apiRequest {
            api.feedbackDetails(
                 getBaseURL(),
                getAuthData()?.id
            )
        }
    }

    suspend fun allFeedbackDetails(): AllFeedBackListResponse {
        return apiRequest {
            api.allFeedbackDetails(
                getBaseURL(),
                2,
                "2023-02-26",
                "2023-03-02"
            )
        }
    }
}