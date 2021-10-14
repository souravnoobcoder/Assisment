package com.example.assisment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assisment.R
import com.example.assisment.Trending
import com.example.assisment.adapter.TrendingAdapter
import com.example.assisment.api.YoutubeApi
import com.example.assisment.api.YoutubeApiMethod
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView=findViewById<RecyclerView>(R.id.trending_list)
        val j=YoutubeApi.retrofit?.create(YoutubeApiMethod::class.java)

        recyclerView.layoutManager=LinearLayoutManager(this@MainActivity)
            j?.getList()?.enqueue(object : Callback<Trending>{
                override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                   val adapter = TrendingAdapter(response.body()?.items!!)
                    recyclerView.adapter=adapter
                }

                override fun onFailure(call: Call<Trending>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}