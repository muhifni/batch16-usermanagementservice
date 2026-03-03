package com.batch16.usermanagementservice.service.impl

import com.batch16.usermanagementservice.domain.dto.req.ReqCreateUserDto
import com.batch16.usermanagementservice.domain.dto.req.ReqUpdateUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResCreateUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResGetUserDetailDto
import com.batch16.usermanagementservice.domain.dto.res.ResGetUserDto
import com.batch16.usermanagementservice.domain.dto.res.ResUserIdDto
import com.batch16.usermanagementservice.domain.entity.MasterUserEntity
import com.batch16.usermanagementservice.exception.BadRequestException
import com.batch16.usermanagementservice.exception.DataNotFoundException
import com.batch16.usermanagementservice.repository.MasterRoleRepository
import com.batch16.usermanagementservice.repository.MasterUserRepository
import com.batch16.usermanagementservice.service.MasterUserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MasterUserServiceImpl(
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository
): MasterUserService {
    val log = LoggerFactory.getLogger(MasterUserServiceImpl::class.java)
    override fun register(req: ReqCreateUserDto): ResCreateUserDto {
        //LOGIKA API

        //ENHANCE -> KETIKA CREATE USER NAMBAH ROLE ID "USER"

        val registeredEmail = masterUserRepository.findByEmail(req.email)
        //cek registered email ada atau tidak
        if(registeredEmail.isPresent){ //jika ada throw error
            throw BadRequestException("Email ${req.email} already exists")
        }
        //email belum terdaftar lanjut ke business proses berikutnya

        //GET ROLE BY NAME
        val userRole = masterRoleRepository.findRoleByName("user").orElseThrow {
            throw RuntimeException("Role user not found")
        }
        val userEntity = MasterUserEntity( //lokal variabel springboot
            fullName = req.fullName,
            email = req.email,
            password = req.password,
            age = req.age,
            role = userRole //assign role
        )
        //INSERT INTO ....
        val userData = masterUserRepository.save( //Representasi data di tabel
            userEntity
        )
        return ResCreateUserDto(
            userId = userData.id!!
        )
    }

    override fun updateUser(req: ReqUpdateUserDto, userId: Int): ResGetUserDto {
        //GET USER BY ID
        val user = masterUserRepository.findById(userId).orElseThrow {
            log.error("User with id $userId not found!!!")
            println("User with id $userId not found!!!")
            RuntimeException("User with id $userId not found!!!")
        }

        //UPDATE ENTITY
        user.email = req.email
        user.password = req.password
        user.age = req.age
        user.fullName = req.fullName

        //SAVE UPDATE DI DB
        val updatedData = masterUserRepository.save(user) //update data

        //RETURN DATA
        return ResGetUserDto(
            userId = updatedData.id!!,
            fullName = updatedData.fullName,
            email = updatedData.email,
            age = updatedData.age
        )
    }

    override fun softDeleteUser(userId: Int): ResUserIdDto {
        //find user by id di db (query db)
        val user = masterUserRepository.findById(userId).orElseThrow {
            RuntimeException("User with id $userId not found!!!")
        } // -> local variabel di springboot
        //set data is_delete = false
        user.isDelete = true
        //save updated data user di db
        masterUserRepository.save(user)
        //return
        return ResUserIdDto(userId)
    }

    override fun getUserById(id: Int): ResGetUserDetailDto {
        val user = masterUserRepository.findById(id).orElseThrow {
            throw DataNotFoundException("User with id $id not found!")
        }

        return ResGetUserDetailDto(
            userId = user.id!!,
            fullName = user.fullName,
            email = user.email,
            age = user.age,
            roleName = user.role!!.name
        )
    }
}