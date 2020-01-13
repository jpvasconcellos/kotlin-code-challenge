package com.arctouch.codechallenge.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.ItemClickInterface
import com.arctouch.codechallenge.util.buildPosterUrl
import com.arctouch.codechallenge.util.load
import kotlinx.android.synthetic.main.movie_item.view.*

class HomeAdapter(
    private val movies: ArrayList<Movie> = ArrayList(),
    var itemClickInterface: ItemClickInterface<Movie>? = null
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate
            itemView.posterImageView.load(movie.posterPath?.buildPosterUrl().orEmpty())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            itemClickInterface?.itemClicked(movies[position])
        }
    }

    fun addAll(newMovies: List<Movie>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}
