package com.pucetec.geomed.services

import com.pucetec.geomed.dto.DoctorRequest
import com.pucetec.geomed.entities.Doctor
import com.pucetec.geomed.entities.DoctorStatus
import com.pucetec.geomed.exceptions.DuplicateResourceException
import com.pucetec.geomed.exceptions.ResourceNotFoundException
import com.pucetec.geomed.repositories.DoctorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.Optional

class DoctorServiceTest {

    private val doctorRepository = mock(DoctorRepository::class.java)
    private val doctorService = DoctorService(doctorRepository)

    @Test
    fun `createDoctor should save and return doctor when no duplicate exists`() {
        val request = DoctorRequest(
            cognitoUsername = "dr_john",
            cedula = "12345678",
            firstName = "John",
            lastName = "Doe",
            phone = "0999999999",
            email = "john.doe@geomed.com",
            specialty = "Cardiology"
        )
        `when`(doctorRepository.existsByCognitoUsername("dr_john")).thenReturn(false)

        val savedDoctor = Doctor(
            id = 1L,
            cognitoUsername = "dr_john",
            cedula = "12345678",
            firstName = "John",
            lastName = "Doe",
            phone = "0999999999",
            email = "john.doe@geomed.com",
            specialty = "Cardiology",
            status = DoctorStatus.ACTIVE
        )
        `when`(doctorRepository.save(any(Doctor::class.java))).thenReturn(savedDoctor)

        val result = doctorService.createDoctor(request)

        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals("dr_john", result.cognitoUsername)
        assertEquals("Cardiology", result.specialty)
        verify(doctorRepository).save(any(Doctor::class.java))
    }

    @Test
    fun `createDoctor should throw exception when duplicate cognitoUsername exists`() {
        val request = DoctorRequest(cognitoUsername = "dr_john")
        `when`(doctorRepository.existsByCognitoUsername("dr_john")).thenReturn(true)

        assertThrows<DuplicateResourceException> {
            doctorService.createDoctor(request)
        }
        verify(doctorRepository, never()).save(any(Doctor::class.java))
    }

    @Test
    fun `getDoctorById should return doctor when found`() {
        val doctor = Doctor(
            id = 1L,
            cognitoUsername = "dr_john",
            firstName = "John"
        )
        `when`(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor))

        val result = doctorService.getDoctorById(1L)

        assertNotNull(result)
        assertEquals("dr_john", result.cognitoUsername)
    }

    @Test
    fun `getDoctorById should throw exception when not found`() {
        `when`(doctorRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            doctorService.getDoctorById(1L)
        }
    }
}
