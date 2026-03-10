package com.batch16.usermanagementservice.domain.dto.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ReqLoginDto(
    @field:NotNull(message = "email is required")
    @field:NotBlank(message = "email must not be blank")
    @field:Email(message = "email must be an email")
    val email: String?,

    @field:NotNull(message = "password is required")
    @field:NotBlank(message = "password must not be blank")
    val password: String?
)
