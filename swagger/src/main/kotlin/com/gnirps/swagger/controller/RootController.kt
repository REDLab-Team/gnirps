package com.gnirps.swagger.controller

import com.gnirps.swagger.config.properties.SwaggerProperties
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import javax.servlet.http.HttpServletRequest

@Tag(
        name = "Root Controller",
        description = ""
)
@Controller
@RequestMapping(RootController.ROOT_PATH)
class RootController(
        private val swaggerProperties: SwaggerProperties
) {
    companion object {
        const val ROOT_PATH: String = "/"
    }

    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    @GetMapping
    @Operation(summary = "Redirect towards an url defined in a property file.")
    @ApiResponse(responseCode = "308", description = "Permanent Redirect")
    fun redirectToDoc(request: HttpServletRequest): ModelAndView {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.PERMANENT_REDIRECT)
        return ModelAndView("redirect:${swaggerProperties.rootRedirect}")
    }
}
