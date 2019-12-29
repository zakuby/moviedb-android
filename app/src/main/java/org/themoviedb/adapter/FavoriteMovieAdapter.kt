package org.themoviedb.adapter

import org.themoviedb.R
import org.themoviedb.core.base.BaseAdapter
import org.themoviedb.data.models.Movie

class FavoriteMovieAdapter(
    clickListener: (Movie) -> Unit,
    val removeFavoriteListener: (Movie) -> Unit
) : BaseAdapter<Movie>(clickListener) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_favorite_movie

    fun removeButtonListener(movie: Movie) = removeFavoriteListener(movie)

    fun removeFromFavorite(item: Movie) = removeItemFromList(item)
}