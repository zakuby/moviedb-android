package org.themoviedb.adapter

import org.themoviedb.R
import org.themoviedb.core.base.BaseAdapter
import org.themoviedb.data.models.Cast

class DetailListCastAdapter : BaseAdapter<Cast>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_cast
}