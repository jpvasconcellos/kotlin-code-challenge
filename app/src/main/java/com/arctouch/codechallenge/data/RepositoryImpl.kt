package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.BuildConfig
import com.arctouch.codechallenge.api.TmdbService
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*

class RepositoryImpl : Repository {

    override fun getGenres(): Single<List<Genre>> {
        return Single.create { emitter ->
            TmdbService.service.genres(BuildConfig.API_KEY, Locale.getDefault().language)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    emitter.onSuccess(it.genres)
                }, {
                    emitter.onError(it)
                })
        }
    }

    override fun getUpcomingMovies(page: Long): Single<List<Movie>> {
        return Single.create { emitter ->
            TmdbService.service.upcomingMovies(
                BuildConfig.API_KEY,
                Locale.getDefault().language,
                page,
                Locale.getDefault().country
            )
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }

                    emitter.onSuccess(moviesWithGenres)
                }, {
                    emitter.onError(it)
                })
        }
    }

}