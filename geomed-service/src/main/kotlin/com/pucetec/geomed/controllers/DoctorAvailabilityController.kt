package com.pucetec.geomed.controllers

import com.pucetec.geomed.dto.DoctorAvailabilityRequest
import com.pucetec.geomed.dto.DoctorAvailabilityResponse
import com.pucetec.geomed.services.DoctorAvailabilityService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/availabilities")
class DoctorAvailabilityController(
    private val doctorAvailabilityService: DoctorAvailabilityService
) {
    private val logger = LoggerFactory.getLogger(DoctorAvailabilityController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAvailability(@Valid @RequestBody request: DoctorAvailabilityRequest): DoctorAvailabilityResponse {
        logger.info("REST request to create DoctorAvailability")
        return doctorAvailabilityService.createAvailability(request)
    }

    @GetMapping("/{id}")
    fun getAvailability(@PathVariable id: Long): DoctorAvailabilityResponse {
        logger.info("REST request to get DoctorAvailability : $id")
        return doctorAvailabilityService.getAvailabilityById(id)
    }

    @get:GetMapping
    val allAvailabilities: List<DoctorAvailabilityResponse>
        get() {
            logger.info("REST request to get all DoctorAvailabilities")
            return doctorAvailabilityService.getAllAvailabilities()
        }

    @GetMapping("/doctor/{doctorId}")
    fun getAvailabilitiesByDoctor(@PathVariable doctorId: Long): List<DoctorAvailabilityResponse> {
        logger.info("REST request to get DoctorAvailabilities for Doctor : $doctorId")
        return doctorAvailabilityService.getAvailabilitiesByDoctorId(doctorId)
    }

    @PutMapping("/{id}")
    fun updateAvailability(
        @PathVariable id: Long,
        @Valid @RequestBody request: DoctorAvailabilityRequest
    ): DoctorAvailabilityResponse {
        logger.info("REST request to update DoctorAvailability : $id")
        return doctorAvailabilityService.updateAvailability(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAvailability(@PathVariable id: Long) {
        logger.info("REST request to delete DoctorAvailability : $id")
        doctorAvailabilityService.deleteAvailability(id)
    }
}
