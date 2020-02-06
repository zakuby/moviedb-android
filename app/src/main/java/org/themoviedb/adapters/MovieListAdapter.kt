package org.themoviedb.adapters

import androidx.recyclerview.widget.DiffUtil
import org.themoviedb.R
import org.themoviedb.ui.base.BaseAdapter
import org.themoviedb.data.local.models.Movie
import org.themoviedb.ui.base.BasePagedListAdapter

class MovieListAdapter(
    clickListener: (Movie) -> Unit
) : BasePagedListAdapter<Movie>(clickListener, Movie.DIFF_CALLBACK) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_movie

}