package com.batch16.usermanagementservice.controller

import com.batch16.usermanagementservice.domain.dto.BaseResponse
import com.batch16.usermanagementservice.domain.dto.req.ReqCreateUserDto
import com.batch16.usermanagementservice.domain.dto.req.ReqUpdateUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResCreateUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResGetUserDetailDto
import com.batch16.usermanagementservice.domain.dto.res.ResGetUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResUserIdDto
import com.batch16.usermanagementservice.exception.GeneralException
import com.batch16.usermanagementservice.service.MasterUserService
import com.fasterxml.jackson.databind.ser.Serializers.Base
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val masterUserService: MasterUserService
) {

    @GetMapping("/hello-world")
    fun helloWorld(): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity.ok(BaseResponse("Hello World"))
    }

    @PostMapping("/register")
    fun register(
      @Valid @RequestBody req: ReqCreateUserDto
    ): ResponseEntity<BaseResponse<ResCreateUserDto>>{
      return ResponseEntity(
          BaseResponse(
              message = "Success",
              data = masterUserService.register(req)
          ),
          HttpStatus.CREATED
      )
    }

    @GetMapping
    fun getAllUsers(
        @RequestHeader("X-ROLE") role: String,
    ): ResponseEntity<BaseResponse<List<ResGetUserDto>>> {
        if (role != "admin") throw GeneralException(HttpStatus.UNAUTHORIZED, "Access Denied")
        return ResponseEntity.ok(
            BaseResponse(
                message = "Success get all users",
                data = masterUserService.getAllUsers()
            )
        )
    }

    @GetMapping("/{userId}")
    fun getUserId(
        @PathVariable userId: Int
    ): ResponseEntity<BaseResponse<ResGetUserDetailDto>>{
        return ResponseEntity.ok(
            BaseResponse(
                message = "Success",
                data = masterUserService.getUserById(userId)
            )
        )
    }

    @PutMapping
    fun updateUser(
        @RequestHeader("X-USER-ID") userId: Int,
        @RequestBody req: ReqUpdateUserDto
    ): ResponseEntity<BaseResponse<ResGetUserDto>>{
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.updateUser(req, userId),
                message = "Success update user"
            )
        )
    }

    @DeleteMapping("/{userId}")
    fun softDelete(
        @RequestHeader("X-ROLE") role: String,
        @PathVariable userId: Int
    ): ResponseEntity<BaseResponse<ResUserIdDto>>{
        if (role != "admin") throw GeneralException(HttpStatus.UNAUTHORIZED, "Access Denied")
        return ResponseEntity.ok(
            BaseResponse(
                message = "Success delete user",
                data = masterUserService.softDeleteUser(userId)
            )
        )
    }


}