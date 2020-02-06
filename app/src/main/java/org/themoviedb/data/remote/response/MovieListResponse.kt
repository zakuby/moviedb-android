package org.themoviedb.data.remote.response

import org.themoviedb.data.local.models.Movie

data class MovieListResponse(
    val page: Int?,
    val results: List<Movie>?
)