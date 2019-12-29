package org.themoviedb.adapter

import org.themoviedb.R
import org.themoviedb.core.base.BaseAdapter
import org.themoviedb.data.models.TvShow

class FavoriteTvShowAdapter(
    clickListener: (TvShow) -> Unit,
    val removeFavoriteListener: (TvShow) -> Unit
) : BaseAdapter<TvShow>(clickListener) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_favorite_tv_show

    fun removeButtonListener(tvShow: TvShow) = removeFavoriteListener(tvShow)

    fun removeFromFavorite(item: TvShow) = removeItemFromList(item)
}