package com.pucetec.users.exceptions

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(e: ResourceNotFoundException): ResponseEntity<ExceptionResponse> {
        logger.warn("event=exception.resource_not_found | msg={}", e.message)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ExceptionResponse(message = e.message ?: "Recurso no encontrado", source = "UsersService"))
    }

    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicateResource(e: DuplicateResourceException): ResponseEntity<ExceptionResponse> {
        logger.warn("event=exception.duplicate_resource | msg={}", e.message)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ExceptionResponse(message = e.message ?: "El recurso ya existe", source = "UsersService"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        val errorMessage = e.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }
        logger.warn("event=exception.validation_error | msg={}", errorMessage)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse(message = errorMessage, source = "UsersValidation"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ExceptionResponse> {
        logger.error("event=exception.internal_error | msg={}", e.message, e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse(message = "Ocurrió un error interno en el servidor", source = "UsersInternal"))
    }
}

data class ExceptionResponse(
    val message: String,
    val source: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)