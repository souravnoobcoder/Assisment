package com.example.assisment.activities

import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assisment.App
import com.example.assisment.R
import com.example.assisment.adapter.VideoAdapter
import com.example.assisment.api.Trending
import com.example.assisment.api.YoutubeApi
import com.example.assisment.api.YoutubeApiMethod
import com.example.assisment.room.Entity
import com.example.assisment.room.VideoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var bo = true
    private var adapter: VideoAdapter? = null
    private var nextPageToken: String? = ""
    private var viewModel: VideoViewModel? = null
    private var api: YoutubeApiMethod? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        Initializing variables
         */
        viewModel = ViewModelProvider(this@MainActivity).get(VideoViewModel::class.java)
        recyclerView = findViewById(R.id.trending_list)
        api = YoutubeApi.retrofit?.create(YoutubeApiMethod::class.java)
        progressBar = findViewById(R.id.progress)
        refreshLayout = findViewById(R.id.refresh_layout)
        recyclerView?.layoutManager = LinearLayoutManager(this@MainActivity)

        /*
        delete previous items if network is connected
        */
        @RequiresApi(Build.VERSION_CODES.M)
        if (App.isOnline())
            deleteAll()

        fetchDataFirstTime()

        /*
        When User comes to the end of the recyclerView
        and scroll up then this invokes it adds new data to
        LocalDatabase and UI
         */
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    fetchNewData(nextPageToken)
                }
            }
        })
        showData()

        /*
        Invokes when you refresh the page
        It deletes every previous data and fill fresh new data on
        localDatabase and on UI
         */
        refreshLayout?.setOnRefreshListener {
            @RequiresApi(Build.VERSION_CODES.M)
            if (App.isOnline()){
                deleteAll()
                fetchDataFirstTime()
                CoroutineScope(Main).launch {
                    delay(500)
                    refreshLayout!!.isRefreshing = false
                }
            }else{
                makeToast("No Connection")
            }

        }
    }

    /*
    fetches data for first time when application is started
    and save results to the local database
     */
    private fun fetchDataFirstTime() {
        api?.getList()?.enqueue(object : Callback<Trending> {
            override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                val videos: MutableList<Entity> = ArrayList()
                for (i in 0 until YoutubeApiMethod.firstCallSize) {
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
                @RequiresApi(Build.VERSION_CODES.M)
                if (App.isOnline())
                    makeToast("Sorry \nTry Again Please!!")
                makeToast("No Connection")
            }
        })
    }

    /*
     Show video list to the user and update itself when needed
    */
    private fun showData() {
        viewModel?.videoList?.observe(this,
            {
                if (bo) {
                    adapter = VideoAdapter(it,this@MainActivity)
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
        for (i in 1 until videos.size) {
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
        progressBar?.visibility = VISIBLE
        api?.getNextList(nextPageToken)?.enqueue(object : Callback<Trending> {
            override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                val videos: MutableList<Entity> = ArrayList()
                for (i in 0 until YoutubeApiMethod.secondCallSize) {
                    val item = response.body()?.items?.get(i)
                    val entity = Entity(
                        thumbnailUrl = item?.snippet?.thumbnails?.medium?.url!!,
                        title = item.snippet.title,
                        videoLink = item.id
                    )
                    videos.add(entity)
                    progressBar?.visibility = GONE
                }
                setRoom(videos)
                this@MainActivity.nextPageToken = response.body()?.nextPageToken
            }

            override fun onFailure(call: Call<Trending>, t: Throwable) {
                @RequiresApi(Build.VERSION_CODES.M)
                if (App.isOnline())
                    makeToast("Sorry \nTry Again Please!!")
                makeToast("No Connection")
                progressBar?.visibility = GONE
            }

        })
    }

    /*
    delete all data of local database
    */
    private fun deleteAll() = CoroutineScope(IO).launch { viewModel?.deleteAll() }
    private fun makeToast(message:String)=Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}