package com.batch16.usermanagementservice.domain.dto.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ReqUpdateUserDto(
    @field:NotBlank(message = "email is required")
    @field:Email(message = "email must be an email")
    val email: String,

    @field:NotBlank(message = "password is required")
    @field:Size(
        min = 8, max = 10,
        message = "Password must have 8-10 characters long")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Password must have at least 1 lowercase, 1 uppercase, 1 number"
    )
    val password: String,

    @field:NotBlank(message = "fullName is required")
    @field:Pattern(
        regexp = "^[A-Za-z]+(?: [A-Za-z]+){0,49}\$",
        message = "Full name must be 3–50 characters long and contain only letters and spaces."
    )
    val fullName: String,

    @field:Pattern(
        regexp = "^(?:1[01][0-9]|120|[1-9][0-9]?)\$",
        message = "Age must be a number between 1 and 120."
    )
    val age: Int? //optional
)
