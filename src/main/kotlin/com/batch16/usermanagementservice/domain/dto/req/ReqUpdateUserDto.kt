package com.batch16.usermanagementservice.domain.dto.req

data class ReqUpdateUserDto(
    val email: String,
    val password: String,
    val fullName: String,
    val age: Int? //optional
)
