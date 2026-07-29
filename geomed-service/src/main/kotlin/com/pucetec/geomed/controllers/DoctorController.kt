package com.pucetec.geomed.controllers

import com.pucetec.geomed.dto.DoctorRequest
import com.pucetec.geomed.dto.DoctorResponse
import com.pucetec.geomed.services.DoctorService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/doctors")
class DoctorController(
    private val doctorService: DoctorService
) {
    private val logger = LoggerFactory.getLogger(DoctorController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createDoctor(@Valid @RequestBody request: DoctorRequest): DoctorResponse {
        logger.info("REST request to create Doctor")
        return doctorService.createDoctor(request)
    }

    @GetMapping("/{id}")
    fun getDoctor(@PathVariable id: Long): DoctorResponse {
        logger.info("REST request to get Doctor : $id")
        return doctorService.getDoctorById(id)
    }

    @get:GetMapping
    val allDoctors: List<DoctorResponse>
        get() {
            logger.info("REST request to get all Doctors")
            return doctorService.getAllDoctors()
        }

    @PutMapping("/{id}")
    fun updateDoctor(@PathVariable id: Long, @Valid @RequestBody request: DoctorRequest): DoctorResponse {
        logger.info("REST request to update Doctor : $id")
        return doctorService.updateDoctor(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDoctor(@PathVariable id: Long) {
        logger.info("REST request to delete Doctor : $id")
        doctorService.deleteDoctor(id)
    }
}
