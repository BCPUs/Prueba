package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.VisitDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface VisitDetailRepository : JpaRepository<VisitDetail, Long> {
    fun findByIdAndDeletedAtIsNull(id: Long): Optional<VisitDetail>
    fun findAllByDeletedAtIsNull(): List<VisitDetail>
    fun findByAppointmentIdAndDeletedAtIsNull(appointmentId: Long): Optional<VisitDetail>
    fun existsByAppointmentIdAndDeletedAtIsNull(appointmentId: Long): Boolean
}