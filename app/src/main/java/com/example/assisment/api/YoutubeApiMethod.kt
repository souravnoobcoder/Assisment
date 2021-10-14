package com.example.assisment.api

import com.example.assisment.Trending
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiMethod {
    @GET("videos?part=snippet&chart=mostPopular&key=AIzaSyBQXBAywfCAu8y3x_Km8xwlPVcSZAifa0A&maxResults=25")
    fun getList(): Call<Trending>

    @GET("https://youtube.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&key=AIzaSyBQXBAywfCAu8y3x_Km8xwlPVcSZAifa0A" +
            "&pageToken=CAMQAA&maxResults=10")
    fun getNextList(@Query("pageToken") pageToken: String) : Call<Trending>
}