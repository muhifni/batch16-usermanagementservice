package com.batch16.usermanagementservice.service

import com.batch16.usermanagementservice.domain.dto.BaseResponse
import com.batch16.usermanagementservice.domain.dto.req.ReqLoginDto
import com.batch16.usermanagementservice.domain.dto.res.ResLoginDto
import org.springframework.http.ResponseEntity

interface AuthService {
    fun Login(req: ReqLoginDto): ResponseEntity<BaseResponse<ResLoginDto>>
}