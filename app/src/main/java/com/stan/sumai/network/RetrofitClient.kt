package com.stan.sumai.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val API_KEY = "AIzaSyCSm-JKG9En7z5H_u0OJ4i4mnUrKsoPbGs"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}
