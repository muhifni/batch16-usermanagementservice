package com.batch16.usermanagementservice.domain.dto.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ReqLoginDto(
    @field:NotBlank(message = "email is required")
    @field:Email(message = "email must be an email")
    val email: String?,

    @field:NotBlank(message = "password is required")
    val password: String?
)
