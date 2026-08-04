package com.pucetec.geomed.mappers

import com.pucetec.geomed.dto.DoctorAvailabilityRequest
import com.pucetec.geomed.dto.DoctorAvailabilityResponse
import com.pucetec.geomed.entities.AvailabilityStatus
import com.pucetec.geomed.entities.Doctor
import com.pucetec.geomed.entities.DoctorAvailability

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