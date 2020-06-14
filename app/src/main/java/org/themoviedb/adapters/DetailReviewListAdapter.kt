package org.themoviedb.adapters

import org.themoviedb.R
import org.themoviedb.data.local.models.Review
import org.themoviedb.ui.base.BasePagedListAdapter

class DetailReviewListAdapter(
    clickListener: (Review) -> Unit
) : BasePagedListAdapter<Review>(clickListener, diffUtil = Review.DIFF_CALLBACK) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_review
}