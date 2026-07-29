package com.pucetec.geomed.services

import com.pucetec.geomed.dto.DoctorRequest
import com.pucetec.geomed.dto.DoctorResponse
import com.pucetec.geomed.entities.DoctorStatus
import com.pucetec.geomed.exceptions.DuplicateResourceException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.mappers.toEntity
import com.pucetec.geomed.mappers.toResponse
import com.pucetec.geomed.repositories.DoctorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DoctorService(
    private val doctorRepository: DoctorRepository
) {
    private val logger = LoggerFactory.getLogger(DoctorService::class.java)

    fun createDoctor(request: DoctorRequest): DoctorResponse {
        logger.info("Creating doctor with cognitoUsername: ${request.cognitoUsername}")
        if (doctorRepository.existsByCognitoUsername(request.cognitoUsername)) {
            throw DuplicateResourceException("Doctor with cognitoUsername '${request.cognitoUsername}' already exists")
        }
        val doctor = request.toEntity()
        return doctorRepository.save(doctor).toResponse()
    }

    @Transactional(readOnly = true)
    fun getDoctorById(id: Long): DoctorResponse {
        logger.info("Fetching doctor with id: $id")
        return doctorRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Doctor with id $id not found") }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllDoctors(): List<DoctorResponse> {
        logger.info("Fetching all doctors")
        return doctorRepository.findAll().map { it.toResponse() }
    }

    fun updateDoctor(id: Long, request: DoctorRequest): DoctorResponse {
        logger.info("Updating doctor with id: $id")
        val doctor = doctorRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Doctor with id $id not found") }

        doctor.cedula = request.cedula
        doctor.firstName = request.firstName
        doctor.lastName = request.lastName
        doctor.phone = request.phone
        doctor.email = request.email
        doctor.specialty = request.specialty
        doctor.status = try {
            DoctorStatus.valueOf(request.status.uppercase())
        } catch (e: Exception) {
            DoctorStatus.ACTIVE
        }

        return doctorRepository.save(doctor).toResponse()
    }

    fun deleteDoctor(id: Long) {
        logger.info("Deleting doctor with id: $id")
        val doctor = doctorRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Doctor with id $id not found") }
        doctorRepository.delete(doctor)
    }
}
