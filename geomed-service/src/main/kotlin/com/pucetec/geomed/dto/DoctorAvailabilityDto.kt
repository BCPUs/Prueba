package com.pucetec.geomed.dto

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalTime

data class DoctorAvailabilityRequest(
    @field:NotNull(message = "Doctor ID is required")
    val doctorId: Long? = null,

    @field:NotNull(message = "Date is required")
    @field:FutureOrPresent(message = "Date must be in the present or future")
    val date: LocalDate? = null,

    @field:NotNull(message = "Start time is required")
    val startTime: LocalTime? = null,

    @field:NotNull(message = "End time is required")
    val endTime: LocalTime? = null,

    val status: String = "AVAILABLE"
)

data class DoctorAvailabilityResponse(
    val id: Long,
    val doctorId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val status: String
)
