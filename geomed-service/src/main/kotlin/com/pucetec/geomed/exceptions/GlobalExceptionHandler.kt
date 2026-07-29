package com.pucetec.geomed.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(e: ResourceNotFoundException): ResponseEntity<ExceptionResponse> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ExceptionResponse(e.message ?: "Resource not found", "GeoMedService"))

    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicateResource(e: DuplicateResourceException): ResponseEntity<ExceptionResponse> =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ExceptionResponse(e.message ?: "Resource already exists", "GeoMedService"))

    @ExceptionHandler(InvalidStatusException::class)
    fun handleInvalidStatus(e: InvalidStatusException): ResponseEntity<ExceptionResponse> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse(e.message ?: "Invalid status value", "GeoMedService"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        val errorMessage = e.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse(errorMessage, "GeoMedValidation"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ExceptionResponse> =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse(e.message ?: "Internal server error", "GeoMedInternal"))
}

data class ExceptionResponse(
    val message: String,
    val source: String,
)
