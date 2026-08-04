package com.pucetec.geomed.services

import com.pucetec.geomed.dto.DoctorRequest
import com.pucetec.geomed.dto.DoctorResponse
import com.pucetec.geomed.entities.DoctorStatus
import com.pucetec.geomed.exceptions.DuplicateResourceException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.mappers.toEntity
import com.pucetec.geomed.mappers.toResponse
import com.pucetec.geomed.repositories.DoctorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class DoctorService(
    private val doctorRepository: DoctorRepository
) {
    private val logger = LoggerFactory.getLogger(DoctorService::class.java)

    fun createDoctor(request: DoctorRequest): DoctorResponse {
        logger.info("event=doctor.service.create.start | msg=Iniciando registro de medico username={}", request.cognitoUsername)

        if (doctorRepository.existsByCognitoUsernameAndDeletedAtIsNull(request.cognitoUsername)) {
            logger.warn("event=doctor.service.create.duplicate | msg=El medico con username '{}' ya existe", request.cognitoUsername)
            throw DuplicateResourceException("El médico con el usuario '${request.cognitoUsername}' ya se encuentra registrado")
        }

        val doctor = request.toEntity()
        val savedDoctor = doctorRepository.save(doctor)
        logger.info("event=doctor.service.create.success | msg=Medico registrado exitosamente id={}", savedDoctor.id)
        return savedDoctor.toResponse()
    }

    @Transactional(readOnly = true)
    fun getDoctorById(id: Long): DoctorResponse {
        logger.info("event=doctor.service.get.start | msg=Buscando medico id={}", id)
        return doctorRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow {
                logger.warn("event=doctor.service.get.not_found | msg=Medico id={} no encontrado", id)
                ResourceNotFoundException("Médico con ID $id no encontrado")
            }
            .toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllDoctors(): List<DoctorResponse> {
        logger.info("event=doctor.service.get_all.start | msg=Consultando listado completo de medicos activos")
        return doctorRepository.findAllByDeletedAtIsNull().map { it.toResponse() }
    }

    fun updateDoctor(id: Long, request: DoctorRequest): DoctorResponse {
        logger.info("event=doctor.service.update.start | msg=Iniciando actualizacion de medico id={}", id)
        val doctor = doctorRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow {
                logger.warn("event=doctor.service.update.not_found | msg=Medico id={} no encontrado", id)
                ResourceNotFoundException("Médico con ID $id no encontrado")
            }

        doctor.cedula = request.cedula
        doctor.firstName = request.firstName
        doctor.lastName = request.lastName
        doctor.phone = request.phone
        doctor.email = request.email
        doctor.specialty = request.specialty
        doctor.status = runCatching { DoctorStatus.valueOf(request.status.uppercase()) }
            .getOrDefault(DoctorStatus.ACTIVE)

        val updatedDoctor = doctorRepository.save(doctor)
        logger.info("event=doctor.service.update.success | msg=Medico id={} actualizado correctamente", id)
        return updatedDoctor.toResponse()
    }

    fun deleteDoctor(id: Long) {
        logger.info("event=doctor.service.delete.start | msg=Iniciando eliminacion logica de medico id={}", id)
        val doctor = doctorRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow {
                logger.warn("event=doctor.service.delete.not_found | msg=Medico id={} no encontrado", id)
                ResourceNotFoundException("Médico con ID $id no encontrado")
            }

        doctor.deletedAt = LocalDateTime.now()
        doctorRepository.save(doctor)
        logger.info("event=doctor.service.delete.success | msg=Medico id={} marcado como eliminado", id)
    }
}