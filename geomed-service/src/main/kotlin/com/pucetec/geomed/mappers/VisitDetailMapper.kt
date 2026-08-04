package com.pucetec.geomed.mappers

import com.pucetec.geomed.dto.VisitDetailRequest
import com.pucetec.geomed.dto.VisitDetailResponse
import com.pucetec.geomed.entities.Appointment
import com.pucetec.geomed.entities.VisitDetail
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun VisitDetail.toResponse(): VisitDetailResponse {

    val appt = this.appointment
    val doctor = appt?.doctor
    val patient = appt?.patient

    val doctorFullName =
        doctor?.let { "${it.firstName} ${it.lastName}".trim() } ?: "N/A"

    val patientFullName =
        patient?.let { "${it.firstName} ${it.lastName}".trim() } ?: "N/A"

    return VisitDetailResponse(
        id = this.id ?: 0L,
        appointmentId = appt?.id ?: 0L,
        appointmentDate = appt?.date ?: LocalDate.now(),
        appointmentTime = appt?.time ?: LocalTime.now(),
        appointmentStatus = appt?.status?.name ?: "UNKNOWN",
        doctorId = doctor?.id ?: 0L,
        doctorName = doctorFullName,
        doctorSpecialty = doctor?.specialty ?: "N/A",
        patientId = patient?.id ?: 0L,
        patientName = patientFullName,
        diagnosis = this.diagnosis,
        treatment = this.treatment,
        prescription = this.prescription,
        observations = this.observations,
        attentionDate = this.attentionDate
    )
}

fun VisitDetailRequest.toEntity(
    appointment: Appointment
): VisitDetail {

    return VisitDetail(
        appointment = appointment,
        diagnosis = this.diagnosis,
        treatment = this.treatment,
        prescription = this.prescription,
        observations = this.observations,
        attentionDate = LocalDateTime.now()
    )
}