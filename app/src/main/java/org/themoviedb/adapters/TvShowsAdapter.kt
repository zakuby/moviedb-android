package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.ui.base.BasePagedListAdapter

class TvShowsAdapter(
    clickListener: (TvShow) -> Unit
) : BasePagedListAdapter<TvShow>(clickListener, TvShow.DIFF_CALLBACK) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_tv_shows
}