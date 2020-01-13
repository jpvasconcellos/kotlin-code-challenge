package com.arctouch.codechallenge.feature.moviedetail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.util.buildBackdropUrl
import com.arctouch.codechallenge.util.buildPosterUrl
import com.arctouch.codechallenge.util.load
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        val MOVIE_TITLE = "movie_title"
        val MOVIE_GENRES = "movie_genres"
        val MOVIE_RELEASE_DATE = "movie_release_date"
        val MOVIE_OVERVIEW = "movie_overview"
        val MOVIE_POSTER = "movie_poster"
        val MOVIE_BACKDROP = "movie_backdrop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        intent.extras?.apply {
            movie_detail_title.text = getString(MOVIE_TITLE)
            movie_detail_genres.text = getString(MOVIE_GENRES, "")
            movie_detail_overview.text = getString(MOVIE_OVERVIEW, "")
            movie_detail_release_date.text = getString(MOVIE_RELEASE_DATE)
            movie_detail_backdrop.load(getString(MOVIE_BACKDROP)?.buildBackdropUrl().orEmpty())
            movie_detail_poster.load(getString(MOVIE_POSTER)?.buildPosterUrl().orEmpty())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
