package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.DoctorAvailability
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DoctorAvailabilityRepository : JpaRepository<DoctorAvailability, Long> {
    fun findByIdAndDeletedAtIsNull(id: Long): java.util.Optional<DoctorAvailability>
    fun findAllByDeletedAtIsNull(): List<DoctorAvailability>
    fun findByDoctorIdAndDeletedAtIsNull(doctorId: Long): List<DoctorAvailability>
}
