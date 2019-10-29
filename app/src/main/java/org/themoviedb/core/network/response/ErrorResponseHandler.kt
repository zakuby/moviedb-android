package org.themoviedb.core.network.response

import com.google.gson.JsonSyntaxException
import org.themoviedb.core.network.response.ErrorResponse.Type.*
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorResponseHandler @Inject constructor() {
    fun handleException(error: Throwable): ErrorResponse {
        return when (error) {
            // Have Error Message
            is HttpException -> {
                when (error.code()) {
                    302 -> ErrorResponse(message = "Hotspot login required.", type = HOTSPOT_LOGIN)
                    else -> ErrorResponse(
                        message = if (error.message().isNullOrBlank()) "Internal server error." else error.message(),
                        type = GENERAL
                    )
                }
            }
            // Error Parsing
            is JsonSyntaxException -> ErrorResponse(
                message = "Something wrong with response data.",
                type = GENERAL
            )
            // Network Timeout
            is SocketTimeoutException -> ErrorResponse(
                message = "Request Timeout.",
                type = REQUEST_TIMEOUT
            )
            // Network Error
            else -> ErrorResponse(
                message = "Anda tidak terhubung dengan jaringan. Silakan coba beberapa saat lagi.",
                type = NO_INTERNET_CONNECTION
            )
        }
    }
}