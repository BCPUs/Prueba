package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PatientRepository : JpaRepository<Patient, Long> {
    fun findByCognitoUsername(cognitoUsername: String): Patient?
    fun existsByCognitoUsername(cognitoUsername: String): Boolean
}
