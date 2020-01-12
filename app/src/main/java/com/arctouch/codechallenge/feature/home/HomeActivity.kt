package com.arctouch.codechallenge.feature.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val linearLayoutManager = LinearLayoutManager(this)
    private var homeAdapter = HomeAdapter(ArrayList())
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        homeViewModel.fetchData()

        homeViewModel.upcomingMovies.observe(this, Observer { movieList ->
            homeAdapter.addAll(movieList)
            progressBar.visibility = View.GONE
        })

        recyclerView.apply {
            adapter = homeAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(createRecyclerViewScrollListener())
        }
    }

    private fun createRecyclerViewScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
            if (totalItemCount == lastVisibleItemPosition + 1) {
                homeViewModel.fetchMoreData()
            }
        }
    }
}
