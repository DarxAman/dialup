package com.dialupdelta.data.network

import androidx.databinding.library.BuildConfig
import com.dialupdelta.data.network.response.SignUpResponse
import com.dialupdelta.data.network.response.get_age_response.AgeResponse
import com.dialupdelta.data.network.response.get_gender_response.GenderResponse
import com.dialupdelta.data.network.response.get_language_response.LanguageResponse
import com.dialupdelta.data.network.response.login_response.LoginResponse
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

    @GET("{baseURL}getAllLanguages")
    suspend fun getLanguageApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<LanguageResponse>

    @GET("{baseURL}getAllGenders")
    suspend fun getGenderApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<GenderResponse>


    @GET("{baseURL}getAllAgeGroups")
    suspend fun getAgeApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<AgeResponse>

    @FormUrlEncoded
    @POST("{baseURL}signup")
    suspend fun signUpApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("fullName") name: String,
        @Field("mail") email: String,
        @Field("password") password: String,
        @Field("genderId") gender: Int,
        @Field("languageId") language: Int
    ): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("{baseURL}login")
    suspend fun loginApi(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
        @Field("mail") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("{baseURL}")
    suspend fun apiLowHigh(
        @Path(value = "baseURL", encoded = true) baseURL: String?,
    ): Response<GetSummaryResponse>



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