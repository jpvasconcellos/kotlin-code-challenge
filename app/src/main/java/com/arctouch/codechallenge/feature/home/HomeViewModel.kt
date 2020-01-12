package com.arctouch.codechallenge.feature.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arctouch.codechallenge.data.Repository
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(val repository: Repository) : ViewModel() {
    var genres = listOf<Genre>()
    var movieList = ArrayList<Movie>()
    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> = _upcomingMovies
    private val disposables = CompositeDisposable()
    private var currentPage: Int = 1
    private var totalPages: Int = 1

    init {
        fetchGenres()
        fetchUpcomingMovies(1)
    }

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
                    _upcomingMovies.postValue(it.first)
                    movieList.addAll(it.first)
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