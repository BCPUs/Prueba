package com.pucetec.geomed.controllers

import com.pucetec.geomed.dto.AppointmentRequest
import com.pucetec.geomed.dto.AppointmentResponse
import com.pucetec.geomed.dto.AppointmentStatusUpdateRequest
import com.pucetec.geomed.services.AppointmentService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/appointments")
class AppointmentController(
    private val appointmentService: AppointmentService
) {
    private val logger = LoggerFactory.getLogger(AppointmentController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAppointment(@Valid @RequestBody request: AppointmentRequest): AppointmentResponse {
        logger.info("REST request to create Appointment")
        return appointmentService.createAppointment(request)
    }

    @GetMapping("/{id}")
    fun getAppointment(@PathVariable id: Long): AppointmentResponse {
        logger.info("REST request to get Appointment : $id")
        return appointmentService.getAppointmentById(id)
    }

    @get:GetMapping
    val allAppointments: List<AppointmentResponse>
        get() {
            logger.info("REST request to get all Appointments")
            return appointmentService.getAllAppointments()
        }

    @PutMapping("/{id}")
    fun updateAppointment(@PathVariable id: Long, @Valid @RequestBody request: AppointmentRequest): AppointmentResponse {
        logger.info("REST request to update Appointment : $id")
        return appointmentService.updateAppointment(id, request)
    }

    @PatchMapping("/{id}/status")
    fun updateAppointmentStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: AppointmentStatusUpdateRequest
    ): AppointmentResponse {
        logger.info("REST request to update status of Appointment : $id to ${request.status}")
        return appointmentService.updateAppointmentStatus(id, request.status)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAppointment(@PathVariable id: Long) {
        logger.info("REST request to delete Appointment : $id")
        appointmentService.deleteAppointment(id)
    }
}
