package com.pucetec.geomed.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "doctor_availabilities")
class DoctorAvailability(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    var doctor: Doctor? = null,

    @Column(nullable = false)
    var date: LocalDate = LocalDate.now(),

    @Column(name = "start_time", nullable = false)
    var startTime: LocalTime = LocalTime.now(),

    @Column(name = "end_time", nullable = false)
    var endTime: LocalTime = LocalTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: AvailabilityStatus = AvailabilityStatus.AVAILABLE
) : BaseEntity()