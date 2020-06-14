package org.themoviedb.data.remote.response

import org.themoviedb.data.local.models.Review

data class ReviewResponse(
    val page: Int?,
    val results: List<Review>?
)