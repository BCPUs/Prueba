package com.pucetec.geomed.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class DoctorRequest(
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

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String = "",

    @field:NotBlank(message = "Specialty is required")
    val specialty: String = "",

    val status: String = "ACTIVE"
)

data class DoctorResponse(
    val id: Long,
    val cognitoUsername: String,
    val cedula: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val specialty: String,
    val status: String
)
