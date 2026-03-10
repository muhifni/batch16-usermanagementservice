package com.batch16.usermanagementservice.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(name = "mst_user")
data class MasterUserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
    var id: Int? = null, //int

    @Column
    var email: String?, //generate/create column "email varchar(255)",

    @Column(name = "full_name")
    var fullName: String?, //create column fullname varchar(255)

    @Column
    var password: String,

    @Column(nullable = true)
    var age: Int? = null,

    //user berelasi many to one ke role
    //1 role bisa dipakai banyak user
    //1 user hanya punya 1 role
    @ManyToOne(fetch = FetchType.LAZY)
    //relasi di representasikan oleh kolom role_id
    @JoinColumn(name = "role_id", nullable = true)
    var role: MasterRoleEntity? = null
): BaseEntity()
