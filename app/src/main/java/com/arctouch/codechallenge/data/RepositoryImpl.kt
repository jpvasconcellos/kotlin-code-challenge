package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.BuildConfig
import com.arctouch.codechallenge.api.TmdbService
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RepositoryImpl : Repository {

    override fun getGenres(): Single<List<Genre>> {
        return Single.create { emitter ->
            TmdbService.service.genres(BuildConfig.API_KEY, BuildConfig.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    emitter.onSuccess(it.genres)
                }, {
                    emitter.onError(it)
                })
        }
    }

    /*
     I wanted to use Locale.getDefault().displayLanguage, but the correct information is
     in Locale.getDefault().toLanguageTag(), which is only available at API 21.
     So I reverted to build config values.
     */
    override fun getUpcomingMovies(page: Int): Single<Pair<List<Movie>, Int>> {
        return Single.create { emitter ->
            TmdbService.service.upcomingMovies(
                BuildConfig.API_KEY,
                BuildConfig.DEFAULT_LANGUAGE,
                page,
                BuildConfig.DEFAULT_COUNTRY
            )
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }

                    emitter.onSuccess(Pair(moviesWithGenres, it.totalPages))
                }, {
                    emitter.onError(it)
                })
        }
    }

}