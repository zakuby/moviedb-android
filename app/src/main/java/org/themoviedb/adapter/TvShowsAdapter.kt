package org.themoviedb.adapter

import org.themoviedb.R
import org.themoviedb.core.base.BaseAdapter
import org.themoviedb.data.models.TvShow

class TvShowsAdapter(
    clickListener: (TvShow) -> Unit
) : BaseAdapter<TvShow>(clickListener) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_tv_shows
}