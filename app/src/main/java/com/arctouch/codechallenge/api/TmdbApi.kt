package com.arctouch.codechallenge.api

import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("genre/movie/list")
    fun genres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<GenreResponse>

    @GET("movie/upcoming")
    fun upcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int, //Converted to Int because totalPages is an Int
        @Query("region") region: String
    ): Single<UpcomingMoviesResponse>

    @GET("movie/{id}")
    fun movie(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<Movie>
}
