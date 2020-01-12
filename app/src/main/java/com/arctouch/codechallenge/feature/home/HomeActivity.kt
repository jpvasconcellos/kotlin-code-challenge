package com.arctouch.codechallenge.feature.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        homeViewModel.fetchData()

        homeViewModel.upcomingMovies.observe(this, Observer { movieList ->
            recyclerView.adapter = HomeAdapter(movieList)
            progressBar.visibility = View.GONE
        })
    }
}
