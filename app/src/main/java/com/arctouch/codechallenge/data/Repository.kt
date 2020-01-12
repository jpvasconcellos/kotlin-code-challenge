package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Single

interface Repository {
    fun getGenres(): Single<List<Genre>>
    fun getUpcomingMovies(page: Int): Single<Pair<List<Movie>, Int>>
}