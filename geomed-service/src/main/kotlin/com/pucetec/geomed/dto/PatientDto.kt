package com.pucetec.geomed.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PatientRequest(
    @field:NotBlank(message = "Cognito username is required")
    val cognitoUsername: String = "",

    @field:NotBlank(message = "Cedula is required")
    @field:Size(min = 5, max = 20, message = "Cedula must be between 5 and 20 characters")
    val cedula: String = "",

    @field:NotBlank(message = "First name is required")
    val firstName: String = "",

    @field:NotBlank(message = "Last name is required")
    val lastName: String = "",

    @field:NotBlank(message = "Phone number is required")
    val phone: String = "",

    @field:NotBlank(message = "Address is required")
    val address: String = "",

    @field:NotBlank(message = "Reference is required")
    val reference: String = "",

    @field:NotBlank(message = "Clinical risk level is required")
    val clinicalRisk: String = ""
)

data class PatientResponse(
    val id: Long,
    val cognitoUsername: String,
    val cedula: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val address: String,
    val reference: String,
    val clinicalRisk: String
)
