package com.example.assisment.activities

import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assisment.App
import com.example.assisment.R
import com.example.assisment.adapter.VideoAdapter
import com.example.assisment.api.Trending
import com.example.assisment.api.YoutubeApi
import com.example.assisment.databinding.ActivityMainBinding
import com.example.assisment.feature.YoutubeViewModel
import com.example.assisment.room.Entity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var nextPageToken: String = ""
    private val viewModel: YoutubeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val videoAdapter = VideoAdapter(this)
        binding.apply {
            trendingList.apply {
                adapter=videoAdapter
                layoutManager=LinearLayoutManager(this@MainActivity)
                /**
                 *  When User comes to the end of the recyclerView and
                 *  @scroll up then this invokes it adds new data to
                 *  LocalDatabase and UI
                 */

                addOnScrollListener(object :RecyclerView.OnScrollListener(){
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (!recyclerView.canScrollVertically(1)) {
                            progress.isVisible=true
                            viewModel.getVideos(nextPageToken)
                            progress.isVisible=false
                        }
                    }
                })
            }
            /**
             * Invokes when you refresh the page
             * @deletes every previous data and fill fresh new data on
             * localDatabase and on UI
             */
            refreshLayout.setOnRefreshListener {
                @RequiresApi(Build.VERSION_CODES.M)
                if (App.isOnline()) {
                    nextPageToken = ""
                    viewModel.getVideos(nextPageToken)
                } else {
                    Toast.makeText(this@MainActivity, "No Connection", Toast.LENGTH_SHORT).show()
                }
                CoroutineScope(Main).launch {
                    delay(600)
                    refreshLayout.isRefreshing = false
                }
            }
        }
        viewModel.getVideos(nextPageToken).observe(this@MainActivity){
            it.data?.let { i -> videoAdapter.update(i) }
        }
    }
}