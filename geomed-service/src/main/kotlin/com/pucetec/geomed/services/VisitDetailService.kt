package com.pucetec.geomed.services

import com.pucetec.geomed.dto.VisitDetailRequest
import com.pucetec.geomed.dto.VisitDetailResponse
import com.pucetec.geomed.entities.AppointmentStatus
import com.pucetec.geomed.exceptions.DuplicateResourceException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.mappers.toEntity
import com.pucetec.geomed.mappers.toResponse
import com.pucetec.geomed.repositories.AppointmentRepository
import com.pucetec.geomed.repositories.VisitDetailRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class VisitDetailService(
    private val visitDetailRepository: VisitDetailRepository,
    private val appointmentRepository: AppointmentRepository
) {
    private val logger = LoggerFactory.getLogger(VisitDetailService::class.java)

    fun createVisitDetail(request: VisitDetailRequest): VisitDetailResponse {
        logger.info("Creating visit detail for appointment id: ${request.appointmentId}")
        val appointmentId = request.appointmentId ?: throw IllegalArgumentException("Appointment ID is required")

        if (visitDetailRepository.existsByAppointmentIdAndDeletedAtIsNull(appointmentId)) {
            throw DuplicateResourceException("Appointment with id $appointmentId already has an active visit detail")
        }

        val appointment = appointmentRepository.findByIdAndDeletedAtIsNull(appointmentId)
            .orElseThrow { ResourceNotFoundException("Appointment with id $appointmentId not found") }

        val visitDetail = request.toEntity(appointment)
        val saved = visitDetailRepository.save(visitDetail)

        appointment.visitDetail = saved
        appointment.status = AppointmentStatus.COMPLETED
        appointmentRepository.save(appointment)

        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun getVisitDetailById(id: Long): VisitDetailResponse {
        logger.info("Fetching visit detail with id: $id")
        return visitDetailRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Visit detail with id $id not found") }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getVisitDetailByAppointmentId(appointmentId: Long): VisitDetailResponse {
        logger.info("Fetching visit detail for appointment id: $appointmentId")
        return visitDetailRepository.findByAppointmentIdAndDeletedAtIsNull(appointmentId)
            .orElseThrow { ResourceNotFoundException("Visit detail not found for appointment id $appointmentId") }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllVisitDetails(): List<VisitDetailResponse> {
        logger.info("Fetching all active visit details")
        return visitDetailRepository.findAllByDeletedAtIsNull().map { it.toResponse() }
    }

    fun updateVisitDetail(id: Long, request: VisitDetailRequest): VisitDetailResponse {
        logger.info("Updating visit detail with id: $id")
        val visitDetail = visitDetailRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Visit detail with id $id not found") }

        val appointmentId = request.appointmentId ?: throw IllegalArgumentException("Appointment ID is required")
        val appointment = appointmentRepository.findByIdAndDeletedAtIsNull(appointmentId)
            .orElseThrow { ResourceNotFoundException("Appointment with id $appointmentId not found") }

        visitDetail.appointment = appointment
        visitDetail.diagnosis = request.diagnosis
        visitDetail.treatment = request.treatment
        visitDetail.prescription = request.prescription
        visitDetail.observations = request.observations

        return visitDetailRepository.save(visitDetail).toResponse()
    }

    fun deleteVisitDetail(id: Long) {
        logger.info("Soft deleting visit detail with id: $id")
        val visitDetail = visitDetailRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Visit detail with id $id not found") }

        visitDetail.deletedAt = LocalDateTime.now()
        visitDetailRepository.save(visitDetail)
    }
}