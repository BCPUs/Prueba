package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.Doctor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DoctorRepository : JpaRepository<Doctor, Long> {
    fun findByIdAndDeletedAtIsNull(id: Long): Optional<Doctor>
    fun findAllByDeletedAtIsNull(): List<Doctor>
    fun findByCognitoUsernameAndDeletedAtIsNull(cognitoUsername: String): Doctor?
    fun existsByCognitoUsernameAndDeletedAtIsNull(cognitoUsername: String): Boolean
}