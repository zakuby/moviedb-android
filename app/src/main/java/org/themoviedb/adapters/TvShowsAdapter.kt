package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.ui.base.BaseAdapter
import org.themoviedb.data.local.models.TvShow

class TvShowsAdapter(
    clickListener: (TvShow) -> Unit
) : BaseAdapter<TvShow>(clickListener) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_tv_shows
}