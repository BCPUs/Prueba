package com.pucetec.geomed.mappers

import com.pucetec.geomed.dto.AppointmentRequest
import com.pucetec.geomed.dto.AppointmentResponse
import com.pucetec.geomed.entities.Appointment
import com.pucetec.geomed.entities.AppointmentStatus
import com.pucetec.geomed.entities.Doctor
import com.pucetec.geomed.entities.Patient
import java.time.LocalDateTime

fun Appointment.toResponse(): AppointmentResponse {
    return AppointmentResponse(
        id = this.id ?: 0L,
        patientId = this.patient?.id ?: 0L,
        doctorId = this.doctor?.id ?: 0L,
        date = this.date,
        time = this.time,
        reason = this.reason,
        observations = this.observations,
        status = this.status.name,
        createdAt = this.createdAt,
        visitDetail = this.visitDetail?.toResponse()
    )
}

fun AppointmentRequest.toEntity(
    patient: Patient,
    doctor: Doctor
): Appointment {

    val parsedStatus = try {
        AppointmentStatus.valueOf(this.status.uppercase())
    } catch (e: Exception) {
        AppointmentStatus.PENDING
    }

    return Appointment(
        patient = patient,
        doctor = doctor,
        date = this.date!!,
        time = this.time!!,
        reason = this.reason,
        observations = this.observations,
        status = parsedStatus,
    )
}