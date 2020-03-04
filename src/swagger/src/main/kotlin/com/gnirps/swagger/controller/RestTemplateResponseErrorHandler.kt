package com.gnirps.swagger.controller

import com.gnirps.commons.exceptions.HttpException
import com.gnirps.commons.logging.service.AbstractLogger
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.http.HttpStatus.Series
import java.io.IOException


@Component
class RestTemplateResponseErrorHandler: ResponseErrorHandler {
    @Throws(IOException::class)
    override fun hasError(httpResponse: ClientHttpResponse): Boolean {
        return httpResponse.statusCode.series() === Series.CLIENT_ERROR ||
                httpResponse.statusCode.series() === Series.SERVER_ERROR
    }

    @Throws(IOException::class)
    override fun handleError(
        response: ClientHttpResponse
    ) {
        response.statusCode.series().let { statusCodeSeries ->
            when (statusCodeSeries) {
                Series.CLIENT_ERROR, Series.SERVER_ERROR -> {
                    throw HttpException(
                        statusCodeSeries,
                        response.statusCode,
                        AbstractLogger.formatMessage(response)
                    )
                }
                else -> return
            }
        }
    }
}
