package com.pucetec.geomed.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "appointments")
class Appointment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    var patient: Patient? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    var doctor: Doctor? = null,

    @Column(nullable = false)
    var date: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    var time: LocalTime = LocalTime.now(),

    @Column(nullable = false)
    var reason: String = "",

    @Column(nullable = true)
    var observations: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: AppointmentStatus = AppointmentStatus.PENDING,

    @OneToOne(mappedBy = "appointment", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var visitDetail: VisitDetail? = null
) : BaseEntity()