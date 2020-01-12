package com.arctouch.codechallenge.feature.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arctouch.codechallenge.data.Repository
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(val repository: Repository) : ViewModel() {
    var genres = listOf<Genre>()
    val upcomingMovies = MutableLiveData<List<Movie>>()
    private val disposables = CompositeDisposable()
    private var currentPage: Int = 1
    private var totalPages: Int = 1

    private fun fetchGenres() {
        disposables.add(
            repository.getGenres().subscribe(
                {
                    genres = it
                },
                {
                    Log.d(
                        HomeViewModel::class.java.simpleName,
                        it.message ?: "Unexpected error requesting genres"
                    )
                })
        )
    }

    private fun fetchUpcomingMovies(page: Int) {
        disposables.add(
            repository.getUpcomingMovies(page).subscribe(
                {
                    upcomingMovies.postValue(it.first)
                    totalPages = it.second

                }, {
                    Log.d(
                        HomeViewModel::class.java.simpleName,
                        it.message ?: "Unexpected error requesting genres"
                    )
                }
            )
        )
    }

    fun fetchData() {
        if (genres.isEmpty()) {
            fetchGenres()
        }

        if (upcomingMovies.getValue().isNullOrEmpty()) {
            fetchUpcomingMovies(currentPage)
        }
    }

    fun fetchMoreData() {
        if (currentPage + 1 <= totalPages) {
            fetchUpcomingMovies(++currentPage)
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}