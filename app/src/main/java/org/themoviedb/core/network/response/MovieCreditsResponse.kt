package org.themoviedb.core.network.response

import org.themoviedb.data.models.Cast

class MovieCreditsResponse(
    val id: Int?,
    val cast: List<Cast>?
)