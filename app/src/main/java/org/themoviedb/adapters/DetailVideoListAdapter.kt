package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.data.local.models.Video
import org.themoviedb.ui.base.BaseAdapter

class DetailVideoListAdapter(private val onClickPlay: (String) -> Unit) : BaseAdapter<Video>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_detail_video

    fun playButtonListener(youtubeUrl: String) = onClickPlay(youtubeUrl)
}