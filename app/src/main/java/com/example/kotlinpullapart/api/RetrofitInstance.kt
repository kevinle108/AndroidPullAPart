package com.example.kotlinpullapart.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://inventoryservice.pullapart.com/"
//private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val api: PullAPartApi by lazy {
        retrofit.create(PullAPartApi::class.java)
    }
}