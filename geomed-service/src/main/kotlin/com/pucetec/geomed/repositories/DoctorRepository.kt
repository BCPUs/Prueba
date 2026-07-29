package com.pucetec.geomed.repositories

import com.pucetec.geomed.entities.Doctor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DoctorRepository : JpaRepository<Doctor, Long> {
    fun findByCognitoUsername(cognitoUsername: String): Doctor?
    fun existsByCognitoUsername(cognitoUsername: String): Boolean
}
