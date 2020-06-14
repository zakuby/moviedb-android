package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.data.local.models.Genre
import org.themoviedb.ui.base.BaseAdapter

class BottomFilterGenreListAdapter : BaseAdapter<Genre>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_genre_filter

    fun onCheck(item: Genre) {
        item.setCheck()
        notifyDataSetChanged()
    }

    fun getCheckedGenres(): String {
        val checkedItems = items.filter { item -> item.isChecked }
        return checkedItems.joinToString(separator = ",") { it.id.toString() }
    }
}