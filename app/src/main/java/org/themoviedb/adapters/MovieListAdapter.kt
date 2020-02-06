package org.themoviedb.adapters

import androidx.recyclerview.widget.DiffUtil
import org.themoviedb.R
import org.themoviedb.ui.base.BaseAdapter
import org.themoviedb.data.local.models.Movie
import org.themoviedb.ui.base.BasePagedListAdapter

class MovieListAdapter(
    clickListener: (Movie) -> Unit
) : BasePagedListAdapter<Movie>(clickListener, DIFF_CALLBACK) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_movie

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}