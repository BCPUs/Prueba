package com.pucetec.geomed.entities

import jakarta.persistence.*

@Entity
@Table(name = "patients")
class Patient(
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
    var address: String = "",

    @Column(nullable = false)
    var reference: String = "",

    @Column(name = "clinical_risk", nullable = false)
    var clinicalRisk: String = ""
) : BaseEntity()