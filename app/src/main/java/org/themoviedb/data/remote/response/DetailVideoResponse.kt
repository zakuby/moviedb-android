package org.themoviedb.data.remote.response

import org.themoviedb.data.local.models.Video

data class DetailVideoResponse(
    val results: List<Video>?
)