package org.themoviedb.adapter

import org.themoviedb.R
import org.themoviedb.core.base.BaseAdapter
import org.themoviedb.data.models.Movie

class MovieListAdapter(
    clickListener: (Movie) -> Unit
) : BaseAdapter<Movie>(clickListener) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_movie
}