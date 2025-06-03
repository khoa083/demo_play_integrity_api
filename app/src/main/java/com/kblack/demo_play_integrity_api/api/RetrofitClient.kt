package com.kblack.demo_play_integrity_api.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kblack.demo_play_integrity_api.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val apiService: APIServices by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .baseUrl(Constant.BASE_URL)
            .build()
            .create(APIServices::class.java)
    }
    private fun createGson(): Gson {
        return GsonBuilder()
            .create()
    }
}