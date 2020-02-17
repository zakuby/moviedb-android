package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.data.local.models.Genre
import org.themoviedb.ui.base.BaseAdapter

class DetailGenreListAdapter : BaseAdapter<Genre>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_genre
}