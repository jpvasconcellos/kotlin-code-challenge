package com.arctouch.codechallenge.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.feature.moviedetail.MovieDetailActivity
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.ItemClickInterface
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity(), ItemClickInterface<Movie> {

    private val homeViewModel: HomeViewModel by viewModel()
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var homeAdapter: HomeAdapter
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        homeAdapter = HomeAdapter(
            movies = homeViewModel.movieList,
            itemClickInterface = this
        )

        recyclerView.apply {
            adapter = homeAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(createRecyclerViewScrollListener())
        }

        homeViewModel.upcomingMovies.observe(this, Observer { movieList ->
            homeAdapter.addAll(movieList)
            progressBar.visibility = View.GONE
        })
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

    override fun itemClicked(item: Movie) {
        val i = Intent(this, MovieDetailActivity::class.java)
        i.apply {
            putExtra(MovieDetailActivity.MOVIE_TITLE, item.title)
            putExtra(MovieDetailActivity.MOVIE_OVERVIEW, item.overview)
            putExtra(MovieDetailActivity.MOVIE_RELEASE_DATE, item.releaseDate)
            putExtra(MovieDetailActivity.MOVIE_POSTER, item.posterPath)
            putExtra(MovieDetailActivity.MOVIE_BACKDROP, item.backdropPath)
            putExtra(MovieDetailActivity.MOVIE_GENRES, item.genres?.joinToString(separator = ", ") { it.name })
        }

        startActivity(i)
    }
}
