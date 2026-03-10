package com.batch16.usermanagementservice.service

import com.batch16.usermanagementservice.domain.dto.req.ReqCreateUserDto
import com.batch16.usermanagementservice.domain.dto.req.ReqUpdateUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResCreateUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResGetUserDetailDto
import com.batch16.usermanagementservice.domain.dto.res.ResGetUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResUserIdDto

interface MasterUserService {
    fun register(req: ReqCreateUserDto): ResCreateUserDto
    fun updateUser(req: ReqUpdateUserDto, userId: Int): ResGetUserDto
    fun softDeleteUser(userId: Int): ResUserIdDto
    fun getUserById(userId: Int): ResGetUserDetailDto
    fun getAllUsers(): List<ResGetUserDto>
}