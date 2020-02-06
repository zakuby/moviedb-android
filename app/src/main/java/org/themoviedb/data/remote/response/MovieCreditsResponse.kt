package org.themoviedb.data.remote.response

import org.themoviedb.data.local.models.Cast

class MovieCreditsResponse(
    val id: Int?,
    val cast: List<Cast>?
)