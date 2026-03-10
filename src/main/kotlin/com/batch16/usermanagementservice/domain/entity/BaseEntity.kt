package com.batch16.usermanagementservice.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@MappedSuperclass
abstract class BaseEntity {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: Timestamp? = null
    @Column
    var createdBy: String? = null

    @UpdateTimestamp  // otomatis diisi saat data diupdate
    @Column(nullable = true)
    val updatedAt: Timestamp? = null
    @Column
    var updatedBy: String? = null

    @Column(nullable = false)
    var isDelete: Boolean = false
    @Column(nullable = true)
    var deletedAt: Timestamp? = null
    @Column
    var deletedBy: Int? = null
}