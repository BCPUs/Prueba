package com.pucetec.geomed.mappers

import com.pucetec.geomed.dto.DoctorRequest
import com.pucetec.geomed.dto.DoctorResponse
import com.pucetec.geomed.entities.Doctor
import com.pucetec.geomed.entities.DoctorStatus

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