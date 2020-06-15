package com.gnirps.swagger.controller

import com.gnirps.logging.exceptions.HttpException
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.http.HttpStatus.Series
import java.io.BufferedReader

@Component
class RestTemplateResponseErrorHandler: ResponseErrorHandler {
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

    private fun formatMessage(response: ClientHttpResponse): String =
        "{" +
                "\"code\": ${response.statusCode}, " +
                "\"status\": \"${response.statusText}\", " +
                "\"body\": \"" + response
                        .body
                        .bufferedReader()
                        .use(BufferedReader::readText) +
                        "\"" +
        "}"
}
