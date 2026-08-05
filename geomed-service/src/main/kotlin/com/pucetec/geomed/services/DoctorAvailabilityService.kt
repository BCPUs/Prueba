package com.pucetec.geomed.services

import com.pucetec.geomed.dto.DoctorAvailabilityRequest
import com.pucetec.geomed.dto.DoctorAvailabilityResponse
import com.pucetec.geomed.entities.AvailabilityStatus
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.mappers.toEntity
import com.pucetec.geomed.mappers.toResponse
import com.pucetec.geomed.repositories.DoctorAvailabilityRepository
import com.pucetec.geomed.repositories.DoctorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DoctorAvailabilityService(
    private val doctorAvailabilityRepository: DoctorAvailabilityRepository,
    private val doctorRepository: DoctorRepository
) {
    private val logger = LoggerFactory.getLogger(DoctorAvailabilityService::class.java)

    fun createAvailability(request: DoctorAvailabilityRequest): DoctorAvailabilityResponse {
        logger.info("Creating availability for doctor id: ${request.doctorId}")
        val doctorId = request.doctorId ?: throw IllegalArgumentException("Doctor ID is required")
        val doctor = doctorRepository.findByIdAndDeletedAtIsNull(doctorId)
            .orElseThrow { ResourceNotFoundException("Doctor with id $doctorId not found") }

        val availability = request.toEntity(doctor)
        return doctorAvailabilityRepository.save(availability).toResponse()
    }

    @Transactional(readOnly = true)
    fun getAvailabilityById(id: Long): DoctorAvailabilityResponse {
        logger.info("Fetching availability with id: $id")
        return doctorAvailabilityRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Availability with id $id not found") }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllAvailabilities(): List<DoctorAvailabilityResponse> {
        logger.info("Fetching all availabilities")
        return doctorAvailabilityRepository.findAllByDeletedAtIsNull().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getAvailabilitiesByDoctorId(doctorId: Long): List<DoctorAvailabilityResponse> {
        logger.info("Fetching availabilities for doctor id: $doctorId")
        return doctorAvailabilityRepository.findByDoctorIdAndDeletedAtIsNull(doctorId).map { it.toResponse() }
    }

    fun updateAvailability(id: Long, request: DoctorAvailabilityRequest): DoctorAvailabilityResponse {
        logger.info("Updating availability with id: $id")
        val availability = doctorAvailabilityRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Availability with id $id not found") }

        val doctorId = request.doctorId ?: throw IllegalArgumentException("Doctor ID is required")
        val doctor = doctorRepository.findByIdAndDeletedAtIsNull(doctorId)
            .orElseThrow { ResourceNotFoundException("Doctor with id $doctorId not found") }

        availability.doctor = doctor
        availability.date = request.date!!
        availability.startTime = request.startTime!!
        availability.endTime = request.endTime!!
        availability.status = try {
            AvailabilityStatus.valueOf(request.status.uppercase())
        } catch (e: Exception) {
            AvailabilityStatus.AVAILABLE
        }

        return doctorAvailabilityRepository.save(availability).toResponse()
    }

    fun deleteAvailability(id: Long) {
        logger.info("Deleting availability with id: $id")
        val availability = doctorAvailabilityRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Availability with id $id not found") }
        availability.deletedAt = java.time.LocalDateTime.now()
        doctorAvailabilityRepository.save(availability)
    }
}
