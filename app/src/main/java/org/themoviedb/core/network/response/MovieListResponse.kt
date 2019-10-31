package org.themoviedb.core.network.response

import org.themoviedb.models.Movie

data class MovieListResponse(
    val page: Int?,
    val results: List<Movie>?
)