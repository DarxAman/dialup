package com.dialupdelta.data.network

import androidx.databinding.library.BuildConfig
import com.dialupdelta.data.network.response.feedback_response.AllFeedBackListResponse
import com.dialupdelta.data.network.response.feedback_response.FeedBackDetailsResponse

import com.dialupdelta.data.network.response.get_gender_response.AgeGenderResponse
import com.dialupdelta.data.network.response.get_journal_response.DeleteJournalResponse
import com.dialupdelta.data.network.response.get_journal_response.GetJournalList
import com.dialupdelta.data.network.response.get_journal_response.GetSingleJournalList
import com.dialupdelta.data.network.response.get_language_response.LanguageResponse
import com.dialupdelta.data.network.response.get_library_response.LibraryResponse
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepListResponse
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepResponse
import com.dialupdelta.data.network.response.get_to_sleep_response.SaveGetToSleepResponse
import com.dialupdelta.data.network.response.intro_video_response.IntroVideoResponse
import com.dialupdelta.data.network.response.login_response.SignUpLoginResponse
import com.dialupdelta.data.network.response.ocean_response.OceanResponse
import com.dialupdelta.data.network.response.otp_response.OtpResponse
import com.dialupdelta.data.network.response.simple_response.SimpleResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SavedSleepEnhancerResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SleepEnhancerProgramListResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SleepEnhancerResponse
import com.dialupdelta.data.network.response.summary.GetSummaryResponse
import com.dialupdelta.data.network.response.wake_up_response.FetchWakeUpSavedResponse
import com.dialupdelta.data.network.response.wake_up_response.WakeUpProgramResponse
import com.dialupdelta.data.network.response.wake_up_response.WakeUpResponse
import com.github.simonpercic.oklog3.OkLogInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyApi {

    @GET("{baseURL}get_all_language")
    suspend fun getLanguageApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<LanguageResponse>

    @GET("{baseURL}info_page_data")
    suspend fun getAgeGenderApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<AgeGenderResponse>

    @FormUrlEncoded
    @POST("{baseURL}register")
    suspend fun signUpApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("full_name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender_id") gender: Int,
        @Field("language_id") language: Int,
        @Field("age_group_id") age: Int
    ): Response<SignUpLoginResponse>

    @FormUrlEncoded
    @POST("{baseURL}login")
    suspend fun loginApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignUpLoginResponse>

    @GET("{baseURL}")
    suspend fun apiLowHigh(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<GetSummaryResponse>

    @GET("{baseURL}get_intro_videos")
    suspend fun getIntroductionVideoApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<IntroVideoResponse>

    @POST("{baseURL}ocean_data")
    suspend fun getOceanDataApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("trait_name") traitName: String
    ): Response<OceanResponse>

    @FormUrlEncoded
    @POST("{baseURL}send_otp")
    suspend fun sendOtpApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("userId") userId: Int,
    ): Response<OtpResponse>

    @FormUrlEncoded
    @POST("{baseURL}verify_otp")
    suspend fun verifyOtpApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("userID") userId: Int,
        @Field("otp") otp: String,
    ): Response<OtpResponse>

    @GET("{baseURL}get_sleep_enhancer_programs")
    suspend fun getSleepEnhancerProgramList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<SleepEnhancerProgramListResponse>

    @FormUrlEncoded
    @POST("{baseURL}sleep_enhancer")
    suspend fun getSleepEnhancerDialogList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("program") program: Int?,
        @Field("duration") duration: Int,
    ): Response<SleepEnhancerResponse>

    @GET("{baseURL}get_sleep_programs_list")
    suspend fun getToSleepList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<GetToSleepResponse>

    @FormUrlEncoded
    @POST("{baseURL}get_to_sleep")
    suspend fun getToSleepVideoList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("gender") gender: Int?,
        @Field("duration") duration: Int,
        @Field("programs") program: Int?
    ): Response<GetToSleepListResponse>

    @FormUrlEncoded
    @POST("{baseURL}get_journal")
    suspend fun getJournalList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") userId: Int?,
    ): Response<GetJournalList>

    @FormUrlEncoded
    @POST("{baseURL}get_single_journal")
    suspend fun getSingleJournalList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") userId: Int?,
    ): Response<GetSingleJournalList>

    @FormUrlEncoded
    @POST("{baseURL}insert_journal")
    suspend fun setInsertJournal(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") userId: Int?,
        @Field("title") gender: String,
        @Field("content") duration: String
    ): Response<GetSingleJournalList>

    @FormUrlEncoded
    @POST("{baseURL}edit_journal")
    suspend fun editJournal(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("id") program: Int?,
        @Field("title") gender: String,
        @Field("content") duration: String
    ): Response<GetSingleJournalList>

    @FormUrlEncoded
    @POST("{baseURL}delete_journal")
    suspend fun deleteJournal(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("id") id: Int?
    ): Response<DeleteJournalResponse>

    @GET("{baseURL}getYoutubeDetailsByLink")
    suspend fun getYoutubeDetailsByLink(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<LibraryResponse>

    @FormUrlEncoded
    @POST("{baseURL}get_to_sleep_saver")
    suspend fun getToSleepSave(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?,
        @Field("gender_id") genderId: Int?,
        @Field("duration_id") durationId: Int?,
        @Field("program_id") programId: Int?,
        @Field("video_id") videoId: Int?,
    ): Response<SimpleResponse>

    @FormUrlEncoded
    @POST("{baseURL}saved_get_to_sleep")
    suspend fun saveGetToSleepApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?,
    ): Response<SaveGetToSleepResponse>

    @FormUrlEncoded
    @POST("{baseURL}sleep_enhancer_saver")
    suspend fun sleepEnhancerSaver(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?,
        @Field("duration") genderId: Int?,
        @Field("program_id") programId: Int?,
        @Field("audio_id") videoId: Int?,
    ): Response<SimpleResponse>

    @FormUrlEncoded
    @POST("{baseURL}saved_sleep_enhancer")
    suspend fun savedSleepEnhancer(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?,
    ): Response<SavedSleepEnhancerResponse>

    @FormUrlEncoded
    @POST("{baseURL}feedback_details")
    suspend fun feedbackDetails(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?
    ): Response<FeedBackDetailsResponse>

    @FormUrlEncoded
    @POST("{baseURL}single_feedback_details")
    suspend fun allFeedbackDetails(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?,
        @Field("date_from") dateForm: String,
        @Field("date_to") dateTo: String
    ): Response<AllFeedBackListResponse>

    @FormUrlEncoded
    @POST("{baseURL}wake_up_programs")
    suspend fun getWakeUpProgramList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?
    ): Response<WakeUpProgramResponse>

    @FormUrlEncoded
    @POST("{baseURL}wake_up_list")
    suspend fun getWakeUpList(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?,
        @Field("gender") gender: Int?,
        @Field("program") program: Int?
    ): Response<WakeUpResponse>

    @FormUrlEncoded
    @POST("{baseURL}fetch_wake_up_saved")
    suspend fun fetchWakeUpSaved(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?
    ): Response<FetchWakeUpSavedResponse>

    @FormUrlEncoded
    @POST("{baseURL}wake_up_saver")
    suspend fun wakeUpSaver(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("user_id") id: Int?,
        @Field("gender") gender: String?,
        @Field("program") program: String?,
        @Field("thumbURL") thumbUrl: String?,
        @Field("videoURL") videoUrl: String?
    ): Response<SimpleResponse>



    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            sessionExpiredInterceptor: SessionExpiredInterceptor
        ): MyApi {

            var logging: HttpLoggingInterceptor? = null
            var okLogInterceptor: OkLogInterceptor? = null

            if (BuildConfig.DEBUG)  {
                logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okLogInterceptor = OkLogInterceptor.builder().build()
            }

            val okHttpBuilder = OkHttpClient.Builder()
//            val okHttpBuilder = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS)
            okHttpBuilder.readTimeout(30, TimeUnit.SECONDS)
            okHttpBuilder.writeTimeout(30, TimeUnit.SECONDS)
            okHttpBuilder.addInterceptor(networkConnectionInterceptor)
            okHttpBuilder.addInterceptor(sessionExpiredInterceptor)

            if (BuildConfig.DEBUG && okLogInterceptor != null && logging != null) {
                okHttpBuilder.addInterceptor(okLogInterceptor)
                okHttpBuilder.addInterceptor(logging)
            }

            val okHttpClient = okHttpBuilder.build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://dummy/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}