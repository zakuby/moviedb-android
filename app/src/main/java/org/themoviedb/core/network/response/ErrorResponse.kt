package org.themoviedb.core.network.response

data class ErrorResponse(
    val code: Int = 500,
    val message: String? = "Internal Server Error.",
    val type: Type
) {
    enum class Type {
        GENERAL,
        HOTSPOT_LOGIN,
        NO_INTERNET_CONNECTION,
        REQUEST_TIMEOUT
    }
}