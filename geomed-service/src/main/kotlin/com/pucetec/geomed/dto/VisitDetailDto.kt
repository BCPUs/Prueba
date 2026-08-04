package com.pucetec.geomed.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class VisitDetailRequest(
    @field:NotNull(message = "Appointment ID is required")
    val appointmentId: Long? = null,

    @field:NotBlank(message = "Diagnosis is required")
    val diagnosis: String = "",

    @field:NotBlank(message = "Treatment is required")
    val treatment: String = "",

    @field:NotBlank(message = "Prescription is required")
    val prescription: String = "",

    val observations: String? = null
)

data class VisitDetailResponse(
    val id: Long,
    val appointmentId: Long,
    val appointmentDate: LocalDate,
    val appointmentTime: LocalTime,
    val appointmentStatus: String,
    val doctorId: Long,
    val doctorName: String,
    val doctorSpecialty: String,
    val patientId: Long,
    val patientName: String,
    val diagnosis: String,
    val treatment: String,
    val prescription: String,
    val observations: String?,
    val attentionDate: LocalDateTime
)