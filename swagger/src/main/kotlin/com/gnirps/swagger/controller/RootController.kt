package com.gnirps.swagger.controller

import com.gnirps.swagger.config.properties.SwaggerProperties
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

@Api(
        tags = ["Root Controller"],
        value = "Root Controller",
        description = ""
)
@Controller
@RequestMapping(RootController.ROOT_PATH)
@ApiIgnore
class RootController(
        private val swaggerProperties: SwaggerProperties
) {
    companion object {
        const val ROOT_PATH: String = "/"
    }

    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    @GetMapping
    @ApiOperation("Redirect towards an url defined in a property file.")
    @ApiResponses(value = [ApiResponse(code = 308, message = "Permanent Redirect")])
    fun redirectToDoc(request: HttpServletRequest): ModelAndView {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.PERMANENT_REDIRECT)
        return ModelAndView("redirect:${swaggerProperties.rootRedirect}")
    }
}
