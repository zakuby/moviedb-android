package org.themoviedb.adapters

import androidx.recyclerview.widget.DiffUtil
import org.themoviedb.R
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.ui.base.BaseAdapter
import org.themoviedb.ui.base.BasePagedListAdapter

class TvShowsAdapter(
    clickListener: (TvShow) -> Unit
) : BasePagedListAdapter<TvShow>(clickListener, DIFF_CALLBACK) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_tv_shows
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShow>() {
            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem == newItem
            }
        }
    }
}