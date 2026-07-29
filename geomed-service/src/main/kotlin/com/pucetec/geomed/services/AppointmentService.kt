package com.pucetec.geomed.services

import com.pucetec.geomed.dto.AppointmentRequest
import com.pucetec.geomed.dto.AppointmentResponse
import com.pucetec.geomed.entities.AppointmentStatus
import com.pucetec.geomed.exceptions.InvalidStatusException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.mappers.toEntity
import com.pucetec.geomed.mappers.toResponse
import com.pucetec.geomed.repositories.AppointmentRepository
import com.pucetec.geomed.repositories.DoctorRepository
import com.pucetec.geomed.repositories.PatientRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

        val patient = patientRepository.findById(patientId)
            .orElseThrow { ResourceNotFoundException("Patient with id $patientId not found") }

        val doctor = doctorRepository.findById(doctorId)
            .orElseThrow { ResourceNotFoundException("Doctor with id $doctorId not found") }

        val appointment = request.toEntity(patient, doctor)
        return appointmentRepository.save(appointment).toResponse()
    }

    @Transactional(readOnly = true)
    fun getAppointmentById(id: Long): AppointmentResponse {
        logger.info("Fetching appointment with id: $id")
        return appointmentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Appointment with id $id not found") }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllAppointments(): List<AppointmentResponse> {
        logger.info("Fetching all appointments")
        return appointmentRepository.findAll().map { it.toResponse() }
    }

    fun updateAppointment(id: Long, request: AppointmentRequest): AppointmentResponse {
        logger.info("Updating appointment with id: $id")
        val appointment = appointmentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Appointment with id $id not found") }

        val patientId = request.patientId ?: throw IllegalArgumentException("Patient ID is required")
        val doctorId = request.doctorId ?: throw IllegalArgumentException("Doctor ID is required")

        val patient = patientRepository.findById(patientId)
            .orElseThrow { ResourceNotFoundException("Patient with id $patientId not found") }

        val doctor = doctorRepository.findById(doctorId)
            .orElseThrow { ResourceNotFoundException("Doctor with id $doctorId not found") }

        appointment.patient = patient
        appointment.doctor = doctor
        appointment.date = request.date!!
        appointment.time = request.time!!
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
        val appointment = appointmentRepository.findById(id)
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
        val appointment = appointmentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Appointment with id $id not found") }
        appointmentRepository.delete(appointment)
    }
}
