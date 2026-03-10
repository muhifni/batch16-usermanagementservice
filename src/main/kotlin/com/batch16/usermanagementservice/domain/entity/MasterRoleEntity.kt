package com.batch16.usermanagementservice.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "mst_roles")
data class MasterRoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
    var id: Int? = null,

    @Column
    var name: String
): BaseEntity()