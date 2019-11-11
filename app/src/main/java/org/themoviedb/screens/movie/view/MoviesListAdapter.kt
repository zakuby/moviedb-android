package org.themoviedb.screens.movie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.themoviedb.databinding.ListItemMovieBinding
import org.themoviedb.models.Movie

class MoviesListAdapter (val onClick: (Movie) -> Unit) : RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {

    private var movies = emptyList<Movie>()

    fun loadMovies(movies: List<Movie>){
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemMovieBinding.inflate(inflater, parent, false)
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

    inner class ViewHolder(private val binding: ListItemMovieBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie) = binding.apply { this.movie = movie }
    }
}