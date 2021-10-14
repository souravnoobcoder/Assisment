package com.example.assisment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assisment.R
import com.example.assisment.Trending
import com.example.assisment.adapter.TrendingAdapter
import com.example.assisment.api.YoutubeApi
import com.example.assisment.api.YoutubeApiMethod
import com.example.assisment.room.Entity
import com.example.assisment.room.VideoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity()  {
    private var viewModel : VideoViewModel? =null
    private var j: YoutubeApiMethod? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView=findViewById<RecyclerView>(R.id.trending_list)
        j=YoutubeApi.retrofit?.create(YoutubeApiMethod::class.java)
        viewModel= ViewModelProvider(this@MainActivity).get(VideoViewModel::class.java)

        recyclerView.layoutManager=LinearLayoutManager(this@MainActivity)

            j?.getList()?.enqueue(object : Callback<Trending>{
                override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                    var videos: List<Entity>? =null
                    for (i in 0..9)
                        videos?.get(i)=Entity()
                    setRoom()
                }

                override fun onFailure(call: Call<Trending>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Problem Occurred", Toast.LENGTH_SHORT).show()
                }

            })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){

                }
            }
        })
        viewModel?.videoList?.observe(this,
            {
                val adapter = TrendingAdapter(it)
                recyclerView.adapter=adapter })
    }
    private fun setRoom(videos: List<Entity>){
        CoroutineScope(IO).launch {
            viewModel?.insert(videos)
        }
    }
}