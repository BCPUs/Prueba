package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.Appointment
import com.pucetec.geomed.entities.AppointmentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime
import java.util.Optional

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {
    fun findByIdAndDeletedAtIsNull(id: Long): Optional<Appointment>
    fun findAllByDeletedAtIsNull(): List<Appointment>
    fun findByPatientIdAndDeletedAtIsNull(patientId: Long): List<Appointment>
    fun findByDoctorIdAndDeletedAtIsNull(doctorId: Long): List<Appointment>

    fun existsByDoctorIdAndDateAndTimeAndStatusNotAndDeletedAtIsNull(
        doctorId: Long,
        date: LocalDate,
        time: LocalTime,
        status: AppointmentStatus
    ): Boolean

    fun existsByPatientIdAndDateAndTimeAndStatusNotAndDeletedAtIsNull(
        patientId: Long,
        date: LocalDate,
        time: LocalTime,
        status: AppointmentStatus
    ): Boolean
}