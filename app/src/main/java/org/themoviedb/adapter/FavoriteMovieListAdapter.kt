package org.themoviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.themoviedb.data.models.Movie
import org.themoviedb.databinding.ListItemFavoriteMovieBinding

class FavoriteMovieListAdapter(
    val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<FavoriteMovieListAdapter.ViewHolder>() {

    private var movies = emptyList<Movie>()

    fun loadMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemFavoriteMovieBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.apply {
            bind(movie)
            itemView.setOnClickListener { onClick(movie) }
        }
    }

    inner class ViewHolder(private val binding: ListItemFavoriteMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) = binding.apply { this.movie = movie }
    }
}