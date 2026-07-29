package com.pucetec.geomed.controllers

import com.pucetec.geomed.dto.VisitDetailRequest
import com.pucetec.geomed.dto.VisitDetailResponse
import com.pucetec.geomed.services.VisitDetailService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/visit-details")
class VisitDetailController(
    private val visitDetailService: VisitDetailService
) {
    private val logger = LoggerFactory.getLogger(VisitDetailController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createVisitDetail(@Valid @RequestBody request: VisitDetailRequest): VisitDetailResponse {
        logger.info("REST request to create VisitDetail")
        return visitDetailService.createVisitDetail(request)
    }

    @GetMapping("/{id}")
    fun getVisitDetail(@PathVariable id: Long): VisitDetailResponse {
        logger.info("REST request to get VisitDetail : $id")
        return visitDetailService.getVisitDetailById(id)
    }

    @get:GetMapping
    val allVisitDetails: List<VisitDetailResponse>
        get() {
            logger.info("REST request to get all VisitDetails")
            return visitDetailService.getAllVisitDetails()
        }

    @PutMapping("/{id}")
    fun updateVisitDetail(@PathVariable id: Long, @Valid @RequestBody request: VisitDetailRequest): VisitDetailResponse {
        logger.info("REST request to update VisitDetail : $id")
        return visitDetailService.updateVisitDetail(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteVisitDetail(@PathVariable id: Long) {
        logger.info("REST request to delete VisitDetail : $id")
        visitDetailService.deleteVisitDetail(id)
    }
}
