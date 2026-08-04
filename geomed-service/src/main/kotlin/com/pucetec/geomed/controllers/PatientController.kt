package com.pucetec.geomed.controllers

import com.pucetec.geomed.dto.PatientRequest
import com.pucetec.geomed.dto.PatientResponse
import com.pucetec.geomed.services.PatientService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/patients")
class PatientController(
    private val patientService: PatientService
) {
    private val logger = LoggerFactory.getLogger(PatientController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPatient(@Valid @RequestBody request: PatientRequest): PatientResponse {
        logger.info("REST request to create Patient")
        return patientService.createPatient(request)
    }

    @GetMapping("/{id}")
    fun getPatient(@PathVariable id: Long): PatientResponse {
        logger.info("REST request to get Patient : $id")
        return patientService.getPatientById(id)
    }

    @GetMapping
    fun getAllPatients(): List<PatientResponse> {
        logger.info("REST request to get all Patients")
        return patientService.getAllPatients()
    }

    @PutMapping("/{id}")
    fun updatePatient(
        @PathVariable id: Long,
        @Valid @RequestBody request: PatientRequest
    ): PatientResponse {
        logger.info("REST request to update Patient : $id")
        return patientService.updatePatient(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePatient(@PathVariable id: Long) {
        logger.info("REST request to delete Patient : $id")
        patientService.deletePatient(id)
    }
}