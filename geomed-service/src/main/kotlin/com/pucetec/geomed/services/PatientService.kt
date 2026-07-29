package com.pucetec.geomed.services

import com.pucetec.geomed.dto.PatientRequest
import com.pucetec.geomed.dto.PatientResponse
import com.pucetec.geomed.exceptions.DuplicateResourceException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.mappers.toEntity
import com.pucetec.geomed.mappers.toResponse
import com.pucetec.geomed.repositories.PatientRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PatientService(
    private val patientRepository: PatientRepository
) {
    private val logger = LoggerFactory.getLogger(PatientService::class.java)

    fun createPatient(request: PatientRequest): PatientResponse {
        logger.info("Creating patient with cognitoUsername: ${request.cognitoUsername}")
        if (patientRepository.existsByCognitoUsername(request.cognitoUsername)) {
            throw DuplicateResourceException("Patient with cognitoUsername '${request.cognitoUsername}' already exists")
        }
        val patient = request.toEntity()
        return patientRepository.save(patient).toResponse()
    }

    @Transactional(readOnly = true)
    fun getPatientById(id: Long): PatientResponse {
        logger.info("Fetching patient with id: $id")
        return patientRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Patient with id $id not found") }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllPatients(): List<PatientResponse> {
        logger.info("Fetching all patients")
        return patientRepository.findAll().map { it.toResponse() }
    }

    fun updatePatient(id: Long, request: PatientRequest): PatientResponse {
        logger.info("Updating patient with id: $id")
        val patient = patientRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Patient with id $id not found") }

        patient.cedula = request.cedula
        patient.firstName = request.firstName
        patient.lastName = request.lastName
        patient.phone = request.phone
        patient.address = request.address
        patient.reference = request.reference
        patient.clinicalRisk = request.clinicalRisk

        return patientRepository.save(patient).toResponse()
    }

    fun deletePatient(id: Long) {
        logger.info("Deleting patient with id: $id")
        val patient = patientRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Patient with id $id not found") }
        patientRepository.delete(patient)
    }
}
