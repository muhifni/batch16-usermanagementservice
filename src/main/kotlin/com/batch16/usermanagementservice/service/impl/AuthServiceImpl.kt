package com.batch16.usermanagementservice.service.impl

import com.batch16.usermanagementservice.domain.dto.BaseResponse
import com.batch16.usermanagementservice.domain.dto.req.ReqLoginDto
import com.batch16.usermanagementservice.domain.dto.res.ResLoginDto
import com.batch16.usermanagementservice.exception.DataNotFoundException
import com.batch16.usermanagementservice.exception.GeneralException
import com.batch16.usermanagementservice.repository.MasterUserRepository
import com.batch16.usermanagementservice.service.AuthService
import com.batch16.usermanagementservice.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val masterUserRepository: MasterUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
): AuthService {
    override fun Login(req: ReqLoginDto): ResponseEntity<BaseResponse<ResLoginDto>> {
        // MENCARI USER BERDASARKAN EMAIL
        val user = masterUserRepository.findByEmail(req.email)
        if (user.isEmpty) {
            throw DataNotFoundException("User with email ${req.email} not found")
        }

        // MENCOCOKAN PASSWORD DARI REQUEST DGN PASSWORD USER DI DB
        if (!passwordEncoder.matches(req.password, user.get().password)) {
            throw GeneralException(HttpStatus.UNAUTHORIZED, "Password does not match user ${req.email}")
        }

        // GENERATE TOKEN DENGAN JWT UTIL
        // disini id sudah pasti ada jika user ditemukan, jadi bisa pakai !!
        // untuk role, karna masih optional, maka gunakan ?
        val token = jwtUtil.generateJwt(user.get().id!!, user.get().role?.name)

        // RETURN
        return ResponseEntity.ok(
            BaseResponse(
                "Success",
                HttpStatus.OK.value(),
                true,
                ResLoginDto(
                    token
                )
            )
        )
    }
}