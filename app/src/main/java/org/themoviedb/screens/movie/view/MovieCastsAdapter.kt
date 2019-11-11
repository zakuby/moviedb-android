package org.themoviedb.screens.movie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.themoviedb.databinding.ListItemCastBinding
import org.themoviedb.models.Cast

class MovieCastsAdapter : RecyclerView.Adapter<MovieCastsAdapter.ViewHolder>() {

    private var casts: List<Cast> = emptyList()

    fun loadCasts(casts: List<Cast>) {
        this.casts = casts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCastBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = casts.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(casts[position])
    }

    inner class ViewHolder(private val binding: ListItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.apply {
                this.cast = cast
            }
        }
    }
}