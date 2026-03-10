package com.batch16.usermanagementservice.domain.dto.res

data class ResGetUserDto (
    val userId: Int,
    val email: String?,
    val fullName: String?,
    val age: Int?
)