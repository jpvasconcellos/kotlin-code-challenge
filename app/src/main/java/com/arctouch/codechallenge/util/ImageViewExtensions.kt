package com.arctouch.codechallenge.util

import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(url: String) =
    Glide.with(this)
        .load(url)
        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
        .into(this)