package com.gnirps.api.cat.controller

import com.gnirps.api.cat.dto.CatRequest
import com.gnirps.api.cat.dto.CatResponse
import com.gnirps.api.cat.mapper.CatMapper
import com.gnirps.api.cat.model.Cat
import com.gnirps.api.cat.service.CatService
import com.gnirps.utils.CustomPageRequest
import com.gnirps.logging.service.Logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(
    name = "Cat Controller",
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

    @PostMapping
    @Operation(
        summary = "Store a new cat."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Cat created"),
            ApiResponse(responseCode = "400", description = "Access denied or validation failed"),
            ApiResponse(responseCode = "401", description = "Unauthorized")]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody catRequest: CatRequest): CatResponse {
        val cat: Cat = catService.create(CatMapper.fromRequest(catRequest))
        logger.info("$cat created", Logger.EventType.OPERATION)
        return CatMapper.toResponse(cat)
    }

    @PostMapping("/random")
    @Operation(summary = "Store a new random cat.")
    @ApiResponse(responseCode = "201", description = "Cat created")
    @ResponseStatus(HttpStatus.CREATED)
    fun createRandom(): CatResponse {
        val cat: Cat = catService.create(catService.randomCat())
        logger.info("$cat created", Logger.EventType.OPERATION)
        return CatMapper.toResponse(cat)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a cat.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cat retrieved"),
            ApiResponse(responseCode = "400", description = "Access denied or validation failed"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Cat not found")
        ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable id: UUID): CatResponse {
        return CatMapper.toResponse(catService.findById(id))
    }

    @GetMapping("")
    @Operation(
        summary = "Retrieve all cats."
    )
    @ApiResponse(responseCode = "200", description = "Cats retrieved")
    @ResponseStatus(HttpStatus.OK)
    fun findAll(
        @Parameter(
            description = "Offset for the pagination of the results.",
            example = "0"
        ) @RequestParam(required = false) page: Int?,
        @Parameter(
            description = "Maximum number of results expected.",
            example = "10"
        ) @RequestParam(required = false) size: Int?,
        @Parameter(
            description = "Ordering direction of the results.",
            example = "asc"
        ) @RequestParam(required = false) direction: String?,
        @Parameter(
            description = "Sorting properties per order of priority.",
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

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a cat.",
        description = "Requires an Admin level token"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Cat deleted"),
            ApiResponse(responseCode = "400", description = "Access denied or validation failed"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Cat not found")
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: UUID): CatResponse {
        logger.info("cat $id removed", Logger.EventType.OPERATION)
        return CatMapper.toResponse(catService.deleteById(id))
    }
}
