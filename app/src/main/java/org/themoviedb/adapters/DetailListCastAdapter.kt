package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.ui.base.BaseAdapter
import org.themoviedb.data.local.models.Cast

class DetailListCastAdapter : BaseAdapter<Cast>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_cast
}