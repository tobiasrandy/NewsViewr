package com.app.newsviewr.network

import com.app.newsviewr.model.News
import com.app.newsviewr.util.Constants.Companion.DOMAIN

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    companion object {
        fun create(): ApiService{
            val okHttpClientBuilder = OkHttpClient.Builder()

            //For development
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(interceptor)

            val authInterceptor = Interceptor { chain ->
                val originalRequest: Request = chain.request()
                val newRequest: Request = originalRequest.newBuilder()
                    .header("accept", "application/json")
                    .build()
                chain.proceed(newRequest)
            }
            okHttpClientBuilder.addInterceptor(authInterceptor)

            val client = okHttpClientBuilder
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(DOMAIN)
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("carousell_news.json")
    suspend fun getNewsList() : Response<MutableList<News>>

}