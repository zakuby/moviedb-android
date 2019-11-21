package org.themoviedb.screens.tvshow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.themoviedb.databinding.ListItemTvShowsBinding
import org.themoviedb.models.TvShow

class TvShowsAdapter(
    val onClick: (TvShow) -> Unit
) : RecyclerView.Adapter<TvShowsAdapter.ViewHolder>() {

    private var tvShows: List<TvShow> = emptyList()

    fun loadTvShows(tvShows: List<TvShow>) {
        this.tvShows = tvShows
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTvShowsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = tvShows.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvShows[position])
    }

    inner class ViewHolder(private val binding: ListItemTvShowsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShow) {
            binding.apply {
                this.tvshow = tvShow
            }
        }
    }
}