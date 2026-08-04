package com.pucetec.geomed.mappers

import com.pucetec.geomed.dto.PatientRequest
import com.pucetec.geomed.dto.PatientResponse
import com.pucetec.geomed.entities.Patient

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