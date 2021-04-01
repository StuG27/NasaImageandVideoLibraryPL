package com.volynkin.nasaimageandvideolibrary.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create


object Network {

    private val okhttpClient = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .client(okhttpClient)
        .baseUrl("https://images-api.nasa.gov")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val api: Api
        get() = retrofit.create()

}