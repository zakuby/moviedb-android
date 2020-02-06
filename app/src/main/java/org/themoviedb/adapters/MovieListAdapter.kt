package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.ui.base.BaseAdapter
import org.themoviedb.data.local.models.Movie

class MovieListAdapter(
    clickListener: (Movie) -> Unit
) : BaseAdapter<Movie>(clickListener) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_movie
}