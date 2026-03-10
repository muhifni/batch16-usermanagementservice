package com.batch16.usermanagementservice.domain.dto.res

import java.io.Serializable

data class ResGetUserDto (
    val userId: Int,
    val email: String?,
    val fullName: String?,
    val roleName: String? = null,
    val age: Int?
): Serializable