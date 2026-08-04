package com.pucetec.geomed.controllers

import com.pucetec.geomed.dto.DoctorRequest
import com.pucetec.geomed.dto.DoctorResponse
import com.pucetec.geomed.services.DoctorService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/doctors")
class DoctorController(
    private val doctorService: DoctorService
) {
    private val logger = LoggerFactory.getLogger(DoctorController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createDoctor(@Valid @RequestBody request: DoctorRequest): DoctorResponse {
        logger.info("event=doctor.controller.create | msg=Peticion REST para registrar medico")
        return doctorService.createDoctor(request)
    }

    @GetMapping("/{id}")
    fun getDoctor(@PathVariable id: Long): DoctorResponse {
        logger.info("event=doctor.controller.get | msg=Peticion REST para obtener medico id={}", id)
        return doctorService.getDoctorById(id)
    }

    @GetMapping
    fun getAllDoctors(): List<DoctorResponse> {
        logger.info("event=doctor.controller.get_all | msg=Peticion REST para listar medicos")
        return doctorService.getAllDoctors()
    }

    @PutMapping("/{id}")
    fun updateDoctor(
        @PathVariable id: Long,
        @Valid @RequestBody request: DoctorRequest
    ): DoctorResponse {
        logger.info("event=doctor.controller.update | msg=Peticion REST para actualizar medico id={}", id)
        return doctorService.updateDoctor(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDoctor(@PathVariable id: Long) {
        logger.info("event=doctor.controller.delete | msg=Peticion REST para eliminar medico id={}", id)
        doctorService.deleteDoctor(id)
    }
}