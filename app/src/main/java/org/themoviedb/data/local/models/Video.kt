package org.themoviedb.data.local.models

data class Video(
    val key: String? = "",
    val name: String? = "",
    val site: String? = ""
){
    private val isYoutube get() = site.equals("youtube", ignoreCase = true)
    val youtubeThumbnail: String? get() = if(isYoutube) "https://img.youtube.com/vi/$key/mqdefault.jpg" else ""
}