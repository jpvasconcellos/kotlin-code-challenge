package com.arctouch.codechallenge.util

import com.arctouch.codechallenge.BuildConfig

fun String.buildPosterUrl() =
    BuildConfig.POSTER_URL + this + "?api_key=" + BuildConfig.API_KEY

fun String.buildBackdropUrl() =
    BuildConfig.BACKDROP_URL + this + "?api_key=" + BuildConfig.API_KEY