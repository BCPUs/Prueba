package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.VisitDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VisitDetailRepository : JpaRepository<VisitDetail, Long> {
    fun findByAppointmentId(appointmentId: Long): VisitDetail?
}
