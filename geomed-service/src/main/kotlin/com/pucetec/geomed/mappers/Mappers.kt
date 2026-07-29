package com.pucetec.geomed.mappers

import com.pucetec.geomed.dto.*
import com.pucetec.geomed.entities.*
import java.time.LocalDateTime

fun Doctor.toResponse(): DoctorResponse {
    return DoctorResponse(
        id = this.id ?: 0L,
        cognitoUsername = this.cognitoUsername,
        cedula = this.cedula,
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone,
        email = this.email,
        specialty = this.specialty,
        status = this.status.name
    )
}

fun DoctorRequest.toEntity(): Doctor {
    val parsedStatus = try {
        DoctorStatus.valueOf(this.status.uppercase())
    } catch (e: Exception) {
        DoctorStatus.ACTIVE
    }
    return Doctor(
        cognitoUsername = this.cognitoUsername,
        cedula = this.cedula,
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone,
        email = this.email,
        specialty = this.specialty,
        status = parsedStatus
    )
}

fun DoctorAvailability.toResponse(): DoctorAvailabilityResponse {
    return DoctorAvailabilityResponse(
        id = this.id ?: 0L,
        doctorId = this.doctor?.id ?: 0L,
        date = this.date,
        startTime = this.startTime,
        endTime = this.endTime,
        status = this.status.name
    )
}

fun DoctorAvailabilityRequest.toEntity(doctor: Doctor): DoctorAvailability {
    val parsedStatus = try {
        AvailabilityStatus.valueOf(this.status.uppercase())
    } catch (e: Exception) {
        AvailabilityStatus.AVAILABLE
    }
    return DoctorAvailability(
        doctor = doctor,
        date = this.date!!,
        startTime = this.startTime!!,
        endTime = this.endTime!!,
        status = parsedStatus
    )
}

fun Patient.toResponse(): PatientResponse {
    return PatientResponse(
        id = this.id ?: 0L,
        cognitoUsername = this.cognitoUsername,
        cedula = this.cedula,
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone,
        address = this.address,
        reference = this.reference,
        clinicalRisk = this.clinicalRisk
    )
}

fun PatientRequest.toEntity(): Patient {
    return Patient(
        cognitoUsername = this.cognitoUsername,
        cedula = this.cedula,
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone,
        address = this.address,
        reference = this.reference,
        clinicalRisk = this.clinicalRisk
    )
}

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

fun AppointmentRequest.toEntity(patient: Patient, doctor: Doctor): Appointment {
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
        createdAt = LocalDateTime.now()
    )
}

fun VisitDetail.toResponse(): VisitDetailResponse {
    return VisitDetailResponse(
        id = this.id ?: 0L,
        appointmentId = this.appointment?.id ?: 0L,
        diagnosis = this.diagnosis,
        treatment = this.treatment,
        prescription = this.prescription,
        observations = this.observations,
        attentionDate = this.attentionDate
    )
}

fun VisitDetailRequest.toEntity(appointment: Appointment): VisitDetail {
    return VisitDetail(
        appointment = appointment,
        diagnosis = this.diagnosis,
        treatment = this.treatment,
        prescription = this.prescription,
        observations = this.observations,
        attentionDate = LocalDateTime.now()
    )
}
