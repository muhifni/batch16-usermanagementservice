package com.batch16.usermanagementservice.repository

import com.batch16.usermanagementservice.domain.entity.MasterUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MasterUserRepository: JpaRepository<MasterUserEntity, Int> {
    /*
    SELECT mu.* FROM mst_user mu WHERE mu.email = :email
     */
    fun findFirstByEmail(email: String): Optional<MasterUserEntity>
    /*
        SELECT mu.* FROM mst_user mu WHERE mu.is_delete = false
     */
    @Query(
        """
            SELECT mu.* FROM mst_user mu 
            WHERE mu.is_delete = false
        """,
        nativeQuery = true
    )
    override fun findAll(): List<MasterUserEntity>

    @Query(
        """
            SELECT mu.* FROM mst_user mu 
            WHERE mu.id = :id 
            AND mu.is_delete = false 
        """,
        nativeQuery = true
    )
    override fun findById(id: Int): Optional<MasterUserEntity>

    @Query(
        """
            SELECT mu FROM MasterUserEntity mu
            WHERE mu.id = :userId
            AND mu.isDelete = false
        """,
        nativeQuery = false
    )
    fun findByIdJPQL(userId: Int): Optional<MasterUserEntity>

    @Query("""
       SELECT mu.* FROM mst_user mu
       WHERE mu.email = :email
       AND mu.is_delete = false
       LIMIT 1  
    """, nativeQuery = true)
    fun findByEmail(email: String): Optional<MasterUserEntity>
}