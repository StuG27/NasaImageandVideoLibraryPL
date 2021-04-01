package com.volynkin.nasaimageandvideolibrary.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {
    @GET("/search")
    fun downloadList(
        @Query("page") page: Int = 1,
        @Query("q") request: String = "",
        @Query("media_type") type: String = "image,video"
    ): Call<String>

    @GET
    fun downloadItemInfo(
        @Url filesUrl: String
    ): Call<String>

}