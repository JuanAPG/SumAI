package com.stan.sumai.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val API_KEY = "AIzaSyDQeY5rZPr9CATMBMdBpYjZW9NWo5h4PlQ"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}
