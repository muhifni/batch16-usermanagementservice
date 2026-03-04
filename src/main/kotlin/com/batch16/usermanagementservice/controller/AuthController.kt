package com.batch16.usermanagementservice.controller

import com.batch16.usermanagementservice.domain.dto.BaseResponse
import com.batch16.usermanagementservice.domain.dto.req.ReqLoginDto
import com.batch16.usermanagementservice.domain.dto.res.ResLoginDto
import com.batch16.usermanagementservice.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun Login(b
        @RequestBody req: ReqLoginDto
    ): ResponseEntity<BaseResponse<ResLoginDto>> {
           return authService.Login(req)
    }
}