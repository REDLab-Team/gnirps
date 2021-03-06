package com.gnirps.api.cat.controller

import com.gnirps.api.cat.config.annotations.CatAdminAccess
import com.gnirps.api.cat.config.annotations.CatUserAccess
import com.gnirps.api.cat.dto.CatRequest
import com.gnirps.api.cat.dto.CatResponse
import com.gnirps.api.cat.mapper.CatMapper
import com.gnirps.api.cat.model.Cat
import com.gnirps.api.cat.service.CatService
import com.gnirps.utils.CustomPageRequest
import com.gnirps.logging.service.Logger
import io.swagger.annotations.*
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Api(
        tags = ["Cat Controller"],
        value = "Cat Controller",
        description = "Handle cats management."
)
@RestController
@RequestMapping(CatController.ROOT_PATH)
class CatController(
        private val catService: CatService,
        private val logger: Logger
) {
    companion object {
        const val ROOT_PATH: String = "/cats"
    }

    @CatUserAccess
    @PostMapping
    @ApiOperation(
            value = "Store a new cat.",
            notes = "Requires a user access level",
            authorizations = [Authorization(value = "Bearer")]
    )
    @ApiResponses(
            ApiResponse(code = 201, message = "Cat created"),
            ApiResponse(code = 400, message = "Access denied or validation failed"),
            ApiResponse(code = 401, message = "Unauthorized")
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody catRequest: CatRequest): CatResponse {
        val cat: Cat = catService.create(CatMapper.fromRequest(catRequest))
        logger.info("$cat created", Logger.EventType.OPERATION)
        return CatMapper.toResponse(cat)
    }

    @CatUserAccess
    @PostMapping("/random")
    @ApiOperation(
            value = "Store a new random cat.",
            notes = "Requires a user access level",
            authorizations = [Authorization(value = "Bearer")]
    )
    @ApiResponses(ApiResponse(code = 201, message = "Cat created"))
    @ResponseStatus(HttpStatus.CREATED)
    fun createRandom(): CatResponse {
        val cat: Cat = catService.create(catService.randomCat())
        logger.info("$cat created", Logger.EventType.OPERATION)
        return CatMapper.toResponse(cat)
    }

    @CatUserAccess
    @GetMapping("/{id}")
    @ApiOperation(
            value = "Retrieve a cat.",
            notes = "Requires a user access level",
            authorizations = [Authorization(value = "Bearer")]
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Cat retrieved"),
            ApiResponse(code = 400, message = "Access denied or validation failed"),
            ApiResponse(code = 401, message = "Unauthorized"),
            ApiResponse(code = 404, message = "Cat not found")
    )
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable id: UUID): CatResponse {
        return CatMapper.toResponse(catService.findById(id))
    }

    //    @UnsecuredAccess
    @GetMapping("")
    @ApiOperation(value = "Retrieve all cats.")
    @ApiResponses(ApiResponse(code = 200, message = "Cats retrieved"))
    @ResponseStatus(HttpStatus.OK)
    fun findAll(
            @ApiParam(
                    value = "Offset for the pagination of the results.",
                    example = "0"
            ) @RequestParam(required = false) page: Int?,
            @ApiParam(
                    value = "Maximum number of results expected.",
                    example = "10"
            ) @RequestParam(required = false) size: Int?,
            @ApiParam(
                    value = "Ordering direction of the results.",
                    example = "asc"
            ) @RequestParam(required = false) direction: String?,
            @ApiParam(
                    value = "Sorting properties per order of priority.",
                    example = "name, birthday"
            ) @RequestParam(required = false) properties: Array<String>?
    ): Page<CatResponse> {
        return catService
                .findAll(
                        CustomPageRequest(
                                page = page,
                                size = size,
                                direction = direction,
                                properties = properties ?: arrayOf("name", "birthday")
                        ).toPageRequest()
                ).map { CatMapper.toResponse(it) }
    }

    @CatAdminAccess
    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Delete a cat.",
            notes = "Requires an admin access level",
            authorizations = [Authorization(value = "Bearer")]
    )
    @ApiResponses(
            ApiResponse(code = 204, message = "Cat deleted"),
            ApiResponse(code = 400, message = "Access denied or validation failed"),
            ApiResponse(code = 401, message = "Unauthorized"),
            ApiResponse(code = 404, message = "Cat not found")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: UUID): CatResponse {
        logger.info("cat $id removed", Logger.EventType.OPERATION)
        return CatMapper.toResponse(catService.deleteById(id))
    }
}
