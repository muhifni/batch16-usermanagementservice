package com.batch16.usermanagementservice.domain.dto.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


data class ReqCreateUserDto(
    @field:NotBlank(message = "email is required")
    @field:Email(message = "email must be an email")
    val email: String?,

    @field:NotBlank(message = "password is required")
    @field:Size(
        min = 8, max = 10,
        message = "Password must have 8-10 characters long")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Password must have at least 1 lowercase, 1 uppercase, 1 number"
    )
    val password: String?,

    @field:NotBlank(message = "fullName is required")
    val fullName: String?,

    val age: Int? //optional
)
