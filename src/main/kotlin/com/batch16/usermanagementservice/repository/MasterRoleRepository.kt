package com.batch16.usermanagementservice.repository

import com.batch16.usermanagementservice.domain.entity.MasterRoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MasterRoleRepository: JpaRepository<MasterRoleEntity, Int> {
    @Query("""
        SELECT R.* FROM mst_roles R
        WHERE lower(R.name) = lower(:name)
        LIMIT 1
    """, nativeQuery = true)
    fun findRoleByName(name: String): Optional<MasterRoleEntity>
}