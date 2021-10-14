package com.example.assisment.api

import com.example.assisment.Trending
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiMethod {

    /*

    fetch the default or first page
     */
    @GET("videos?part=snippet&chart=mostPopular&key=$apiKey&maxResults=25&regionCode=IN")
    fun getList(): Call<Trending>

    /*
    fetch page with new pageToken
    */
    @GET(
        "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&key=$apiKey" +
                "&pageToken=CAMQAA&maxResults=10"
    )
    fun getNextList(@Query("pageToken") pageToken: String?): Call<Trending>

    companion object {
        const val apiKey = "AIzaSyBQXBAywfCAu8y3x_Km8xwlPVcSZAifa0A"
    }
}