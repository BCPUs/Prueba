package com.pucetec.geomed.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "visit_details")
class VisitDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(optional = false)
    @JoinColumn(name = "appointment_id", unique = true, nullable = false)
    var appointment: Appointment? = null,

    @Column(nullable = false)
    var diagnosis: String = "",

    @Column(nullable = false)
    var treatment: String = "",

    @Column(nullable = false)
    var prescription: String = "",

    @Column(nullable = true)
    var observations: String? = null,

    @Column(name = "attention_date", nullable = false)
    var attentionDate: LocalDateTime = LocalDateTime.now()
)
