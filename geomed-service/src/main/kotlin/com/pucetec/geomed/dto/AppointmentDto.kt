package com.pucetec.geomed.dto

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime

data class AppointmentRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long? = null,

    @field:NotNull(message = "Doctor ID is required")
    val doctorId: Long? = null,

    @field:NotNull(message = "Date is required")
    @field:FutureOrPresent(message = "Appointment date must be in the present or future")
    val date: LocalDate? = null,

    @field:NotNull(message = "Time is required")
    val time: LocalTime? = null,

    @field:NotBlank(message = "Reason is required")
    val reason: String = "",

    val observations: String? = null,

    val status: String = "PENDING"
)

data class AppointmentStatusUpdateRequest(
    @field:NotBlank(message = "Status is required")
    val status: String = ""
)

data class AppointmentResponse(
    val id: Long,
    val patientId: Long,
    val doctorId: Long,
    val date: LocalDate,
    val time: LocalTime,
    val reason: String,
    val observations: String?,
    val status: String,
    val createdAt: LocalDateTime,
    val visitDetail: VisitDetailResponse? = null
)
