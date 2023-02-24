package com.dialupdelta.data.network

import androidx.databinding.library.BuildConfig

import com.dialupdelta.data.network.response.get_gender_response.AgeGenderResponse
import com.dialupdelta.data.network.response.get_language_response.LanguageResponse
import com.dialupdelta.data.network.response.intro_video_response.IntroVideoResponse
import com.dialupdelta.data.network.response.login_response.SignUpLoginResponse
import com.dialupdelta.data.network.response.ocean_response.OceanResponse
import com.dialupdelta.data.network.response.otp_response.OtpResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SleepEnhancerProgramListResponse
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SleepEnhancerResponse
import com.dialupdelta.data.network.response.summary.GetSummaryResponse
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



    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            sessionExpiredInterceptor: SessionExpiredInterceptor
        ): MyApi {

            var logging: HttpLoggingInterceptor? = null
            var okLogInterceptor: OkLogInterceptor? = null

            if (BuildConfig.DEBUG) {
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