package com.pucetec.geomed.services

import com.pucetec.geomed.dto.AppointmentRequest
import com.pucetec.geomed.dto.AppointmentResponse
import com.pucetec.geomed.entities.AppointmentStatus
import com.pucetec.geomed.exceptions.DuplicateResourceException
import com.pucetec.geomed.exceptions.InvalidStatusException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.exceptions.*
import com.pucetec.geomed.mappers.toEntity
import com.pucetec.geomed.mappers.toResponse
import com.pucetec.geomed.repositories.AppointmentRepository
import com.pucetec.geomed.repositories.DoctorRepository
import com.pucetec.geomed.repositories.PatientRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class AppointmentService(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository,
    private val doctorRepository: DoctorRepository
) {
    private val logger = LoggerFactory.getLogger(AppointmentService::class.java)

    fun createAppointment(request: AppointmentRequest): AppointmentResponse {
        logger.info("Creating appointment for patient id: ${request.patientId} and doctor id: ${request.doctorId}")
        val patientId = request.patientId ?: throw IllegalArgumentException("Patient ID is required")
        val doctorId = request.doctorId ?: throw IllegalArgumentException("Doctor ID is required")
        val date = request.date ?: throw IllegalArgumentException("Date is required")
        val time = request.time ?: throw IllegalArgumentException("Time is required")

        val patient = patientRepository.findByIdAndDeletedAtIsNull(patientId)
            .orElseThrow { ResourceNotFoundException("Patient with id $patientId not found") }

        val doctor = doctorRepository.findByIdAndDeletedAtIsNull(doctorId)
            .orElseThrow { ResourceNotFoundException("Doctor with id $doctorId not found") }

        validateTimeSlotAvailability(doctorId, patientId, date, time)

        val appointment = request.toEntity(patient, doctor)
        return appointmentRepository.save(appointment).toResponse()
    }

    @Transactional(readOnly = true)
    fun getAppointmentById(id: Long): AppointmentResponse {
        logger.info("Fetching appointment with id: $id")
        return appointmentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Appointment with id $id not found") }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllAppointments(): List<AppointmentResponse> {
        logger.info("Fetching all active appointments")
        return appointmentRepository.findAllByDeletedAtIsNull().map { it.toResponse() }
    }

    fun updateAppointment(id: Long, request: AppointmentRequest): AppointmentResponse {
        logger.info("Updating appointment with id: $id")
        val appointment = appointmentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Appointment with id $id not found") }

        val patientId = request.patientId ?: throw IllegalArgumentException("Patient ID is required")
        val doctorId = request.doctorId ?: throw IllegalArgumentException("Doctor ID is required")
        val date = request.date ?: throw IllegalArgumentException("Date is required")
        val time = request.time ?: throw IllegalArgumentException("Time is required")

        val patient = patientRepository.findByIdAndDeletedAtIsNull(patientId)
            .orElseThrow { ResourceNotFoundException("Patient with id $patientId not found") }

        val doctor = doctorRepository.findByIdAndDeletedAtIsNull(doctorId)
            .orElseThrow { ResourceNotFoundException("Doctor with id $doctorId not found") }

        if (appointment.date != date || appointment.time != time || appointment.doctor?.id != doctorId) {
            validateTimeSlotAvailability(doctorId, patientId, date, time)
        }

        appointment.patient = patient
        appointment.doctor = doctor
        appointment.date = date
        appointment.time = time
        appointment.reason = request.reason
        appointment.observations = request.observations
        appointment.status = try {
            AppointmentStatus.valueOf(request.status.uppercase())
        } catch (e: Exception) {
            AppointmentStatus.PENDING
        }

        return appointmentRepository.save(appointment).toResponse()
    }

    fun updateAppointmentStatus(id: Long, statusStr: String): AppointmentResponse {
        logger.info("Updating status of appointment with id $id to $statusStr")
        val appointment = appointmentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Appointment with id $id not found") }

        val newStatus = try {
            AppointmentStatus.valueOf(statusStr.uppercase())
        } catch (e: Exception) {
            throw InvalidStatusException("Invalid appointment status: '$statusStr'")
        }

        appointment.status = newStatus
        return appointmentRepository.save(appointment).toResponse()
    }

    fun deleteAppointment(id: Long) {
        logger.info("Deleting appointment with id: $id")
        val appointment = appointmentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow { ResourceNotFoundException("Appointment with id $id not found") }

        appointment.deletedAt = LocalDateTime.now()
        appointmentRepository.save(appointment)
    }

    private fun validateTimeSlotAvailability(
        doctorId: Long,
        patientId: Long,
        date: java.time.LocalDate,
        time: java.time.LocalTime
    ) {
        if (appointmentRepository.existsByDoctorIdAndDateAndTimeAndStatusNotAndDeletedAtIsNull(
                doctorId, date, time, AppointmentStatus.CANCELLED
            )
        ) {
            throw DuplicateResourceException("El médico ya tiene una cita agendada en la fecha $date a las $time")
        }

        if (appointmentRepository.existsByPatientIdAndDateAndTimeAndStatusNotAndDeletedAtIsNull(
                patientId, date, time, AppointmentStatus.CANCELLED
            )
        ) {
            throw DuplicateResourceException("El paciente ya tiene otra cita agendada en la fecha $date a las $time")
        }
    }
}