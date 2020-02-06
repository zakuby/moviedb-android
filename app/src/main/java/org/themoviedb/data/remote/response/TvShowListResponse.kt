package org.themoviedb.data.remote.response

import org.themoviedb.data.local.models.TvShow

data class TvShowListResponse(
    val page: Int?,
    val results: List<TvShow>?
)