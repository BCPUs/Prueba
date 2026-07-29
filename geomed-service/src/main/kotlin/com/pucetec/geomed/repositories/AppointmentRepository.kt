package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.Appointment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {
    fun findByPatientId(patientId: Long): List<Appointment>
    fun findByDoctorId(doctorId: Long): List<Appointment>
}
