package org.themoviedb.data.remote.response

import org.themoviedb.data.local.models.Genre

data class GenreListResponse(
    val genres: List<Genre>?
)