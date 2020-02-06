package org.themoviedb.data.remote.response

import android.content.res.Resources
import com.google.gson.JsonSyntaxException
import org.themoviedb.R
import org.themoviedb.data.remote.response.ErrorResponse.Type.*
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorResponseHandler @Inject constructor(
    private val resources: Resources
) {

    fun handleException(error: Throwable): ErrorResponse {
        return when (error) {
            // Have Error Message
            is HttpException -> {
                when (error.code()) {
                    302 -> ErrorResponse(
                        message = resources.getString(R.string.error_hotspot_login),
                        type = HOTSPOT_LOGIN
                    )
                    else -> ErrorResponse(
                        message =
                        if (error.message().isNullOrBlank()) resources.getString(R.string.error_internal)
                        else error.message(),
                        type = GENERAL
                    )
                }
            }
            // Error Parsing
            is JsonSyntaxException -> ErrorResponse(
                message = resources.getString(R.string.error_response_data),
                type = GENERAL
            )
            // Network Timeout
            is SocketTimeoutException -> ErrorResponse(
                message = resources.getString(R.string.error_request_timeout),
                type = REQUEST_TIMEOUT
            )
            // Network Error
            else -> ErrorResponse(
                message = resources.getString(R.string.error_no_internet_connection),
                type = NO_INTERNET_CONNECTION
            )
        }
    }
}