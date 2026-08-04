package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PatientRepository : JpaRepository<Patient, Long> {
    fun findByIdAndDeletedAtIsNull(id: Long): Optional<Patient>
    fun findAllByDeletedAtIsNull(): List<Patient>
    fun findByCognitoUsernameAndDeletedAtIsNull(cognitoUsername: String): Patient?
    fun existsByCognitoUsernameAndDeletedAtIsNull(cognitoUsername: String): Boolean
}