package org.themoviedb.core.network.response

import org.themoviedb.models.TvShow

data class TvShowListResponse(
    val page: Int?,
    val results: List<TvShow>?
)