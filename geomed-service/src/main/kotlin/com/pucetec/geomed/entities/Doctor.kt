package com.pucetec.geomed.entities

import jakarta.persistence.*

@Entity
@Table(name = "doctors")
class Doctor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "cognito_username", unique = true, nullable = false)
    val cognitoUsername: String = "",

    @Column(nullable = false)
    var cedula: String = "",

    @Column(name = "first_name", nullable = false)
    var firstName: String = "",

    @Column(name = "last_name", nullable = false)
    var lastName: String = "",

    @Column(nullable = false)
    var phone: String = "",

    @Column(nullable = false)
    var email: String = "",

    @Column(nullable = false)
    var specialty: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: DoctorStatus = DoctorStatus.ACTIVE,

    @OneToMany(mappedBy = "doctor", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var availabilities: MutableList<DoctorAvailability> = mutableListOf()
) : BaseEntity()