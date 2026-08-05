package com.pucetec.geomed.services

import com.pucetec.geomed.dto.PatientRequest
import com.pucetec.geomed.entities.Patient
import com.pucetec.geomed.exceptions.DuplicateResourceException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.repositories.PatientRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.Optional

class PatientServiceTest {

    private val patientRepository = mock(PatientRepository::class.java)
    private val patientService = PatientService(patientRepository)

    @Test
    fun `createPatient should save and return patient when valid`() {
        val request = PatientRequest(
            cognitoUsername = "patient_alice",
            cedula = "87654321",
            firstName = "Alice",
            lastName = "Smith",
            phone = "0988888888",
            address = "123 Main St",
            reference = "Near Central Park",
            clinicalRisk = "MEDIUM"
        )
        `when`(patientRepository.existsByCognitoUsernameAndDeletedAtIsNull("patient_alice")).thenReturn(false)

        val savedPatient = Patient(
            id = 1L,
            cognitoUsername = "patient_alice",
            cedula = "87654321",
            firstName = "Alice",
            lastName = "Smith",
            phone = "0988888888",
            address = "123 Main St",
            reference = "Near Central Park",
            clinicalRisk = "MEDIUM"
        )
        `when`(patientRepository.save(any(Patient::class.java))).thenReturn(savedPatient)

        val result = patientService.createPatient(request)

        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals("patient_alice", result.cognitoUsername)
        assertEquals("MEDIUM", result.clinicalRisk)
        verify(patientRepository).save(any(Patient::class.java))
    }

    @Test
    fun `createPatient should throw exception when duplicate exists`() {
        val request = PatientRequest(cognitoUsername = "patient_alice")
        `when`(patientRepository.existsByCognitoUsernameAndDeletedAtIsNull("patient_alice")).thenReturn(true)

        assertThrows<DuplicateResourceException> {
            patientService.createPatient(request)
        }
    }

    @Test
    fun `getPatientById should return patient when found`() {
        val patient = Patient(id = 1L, cognitoUsername = "patient_alice")
        `when`(patientRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(patient))

        val result = patientService.getPatientById(1L)

        assertNotNull(result)
        assertEquals("patient_alice", result.cognitoUsername)
    }

    @Test
    fun `getPatientById should throw exception when not found`() {
        `when`(patientRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            patientService.getPatientById(1L)
        }
    }
}
