package com.pucetec.geomed.entities

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: LocalDateTime? = null
)