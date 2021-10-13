package com.example.assisment.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YoutubeApi {
    private const val BASE_URL = "https://developers.google.com/youtube/v3/"
    private var youtubeApiInstance: Retrofit? = null
    val retrofit: Retrofit?
        get() {
          if (youtubeApiInstance==null){
            youtubeApiInstance=Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build()
          }
          return youtubeApiInstance
        }
}