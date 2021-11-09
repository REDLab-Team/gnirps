package com.gnirps.core.controller

import com.gnirps.logging.exceptions.HttpException
import com.gnirps.utils.isValidJson
import org.springframework.http.HttpStatus.Series
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import java.io.BufferedReader

@Component
class RestTemplateResponseErrorHandler : ResponseErrorHandler {
    override fun hasError(httpResponse: ClientHttpResponse): Boolean {
        return httpResponse.statusCode.series() === Series.CLIENT_ERROR ||
            httpResponse.statusCode.series() === Series.SERVER_ERROR
    }

    override fun handleError(response: ClientHttpResponse) {
        response.statusCode.series().let { statusCodeSeries ->
            when (statusCodeSeries) {
                Series.CLIENT_ERROR, Series.SERVER_ERROR -> {
                    throw HttpException(
                        statusCodeSeries,
                        response.statusCode,
                        formatMessage(response)
                    )
                }
                else -> return
            }
        }
    }

    private fun formatMessage(response: ClientHttpResponse): String {
        var body = response
            .body
            .bufferedReader()
            .use(BufferedReader::readText)

        // Surround with double quotes if the body is not a valid JSON
        if (!body.isValidJson()) {
            body = "\"$body\""
        }

        return "{" +
            "\"code\": ${response.statusCode.value()}, " +
            "\"status\": \"${response.statusText}\", " +
            "\"body\": $body" +
            "}"
    }
}
