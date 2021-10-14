package com.example.assisment.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


class MainActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    private var bo = true
    var adapter: TrendingAdapter? = null
    private var nextPageToken: String? = ""
    private var viewModel: VideoViewModel? = null
    private var j: YoutubeApiMethod? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this@MainActivity).get(VideoViewModel::class.java)
        deleteAll()
        recyclerView = findViewById<RecyclerView>(R.id.trending_list)
        j = YoutubeApi.retrofit?.create(YoutubeApiMethod::class.java)


        recyclerView?.layoutManager = LinearLayoutManager(this@MainActivity)
        fetchDataFirstTime()

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    fetchNewData(nextPageToken)
                }
            }
        })
        showData()

    }

    /*
    fetches data for first time when application is started
    and save results to the local database
     */
    private fun fetchDataFirstTime() {
        j?.getList()?.enqueue(object : Callback<Trending> {
            override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                val videos: MutableList<Entity> = ArrayList()
                for (i in 0..24) {
                    val item = response.body()?.items?.get(i)
                    val entity = Entity(
                        thumbnailUrl = item?.snippet?.thumbnails?.medium?.url!!,
                        title = item.snippet.title,
                        videoLink = item.id
                    )
                    videos.add(entity)
                }
                setRoom(videos)
                nextPageToken = response.body()?.nextPageToken
            }

            override fun onFailure(call: Call<Trending>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Problem Occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*
     show video list to the user and update itself when needed
    */
    private fun showData() {
        viewModel?.videoList?.observe(this,
            {
                if (bo) {
                    adapter = TrendingAdapter(it,this@MainActivity)
                    recyclerView?.adapter = adapter
                    bo = false
                } else {
                    adapter?.update(it)
                }
            })
    }

    /*
    insert a list of video objects in local database
     */
    private fun setRoom(videos: MutableList<Entity>) {
        for (i in 1..9) {
            CoroutineScope(IO).launch {
                viewModel?.insert(videos[i])
            }
        }

    }

    /*
    fetches new data with the nextPageToken
    and save it to local database
    */
    private fun fetchNewData(nextPageToken: String?) {
        j?.getNextList(nextPageToken)?.enqueue(object : Callback<Trending> {
            override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                val videos: MutableList<Entity> = ArrayList()
                for (i in 0..9) {
                    val item = response.body()?.items?.get(i)
                    val entity = Entity(
                        thumbnailUrl = item?.snippet?.thumbnails?.medium?.url!!,
                        title = item.snippet.title,
                        videoLink = item.id
                    )
                    videos.add(entity)
                }
                setRoom(videos)
                this@MainActivity.nextPageToken = response.body()?.nextPageToken
            }

            override fun onFailure(call: Call<Trending>, t: Throwable) {
                Toast.makeText(this@MainActivity, "No new data", Toast.LENGTH_SHORT).show()
            }

        })
    }

    /*
    delete all data of local database
    */
    private fun deleteAll() = CoroutineScope(IO).launch { viewModel?.deleteAll() }
}