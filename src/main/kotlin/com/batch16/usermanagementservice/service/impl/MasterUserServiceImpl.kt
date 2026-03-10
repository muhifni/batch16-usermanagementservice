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
import com.batch16.usermanagementservice.exception.GeneralException
import com.batch16.usermanagementservice.producer.KafkaProducer
import com.batch16.usermanagementservice.repository.MasterRoleRepository
import com.batch16.usermanagementservice.repository.MasterUserRepository
import com.batch16.usermanagementservice.service.MasterUserService
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class MasterUserServiceImpl(
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val kafkaProducer: KafkaProducer<Any>,
): MasterUserService {
    val log = LoggerFactory.getLogger(MasterUserServiceImpl::class.java)
    override fun register(req: ReqCreateUserDto): ResCreateUserDto {
        //LOGIKA API
        //ENHANCE -> KETIKA CREATE USER NAMBAH ROLE ID "USER"

        val registeredEmail = masterUserRepository.findByEmail(req.email)
        //cek registered email ada atau tidak
        if(registeredEmail.isPresent){ //jika ada throw error
            throw GeneralException(HttpStatus.CONFLICT, "Email ${req.email} already exists")
        }
        //email belum terdaftar lanjut ke business proses berikutnya

        //GET ROLE BY NAME
        val userRole = masterRoleRepository.findRoleByName("user").orElseThrow {
            throw GeneralException(HttpStatus.NOT_FOUND, "Role user not found")
        }

        val hashed = passwordEncoder.encode(req.password)
        val userEntity = MasterUserEntity( //lokal variabel springboot
            fullName = req.fullName,
            email = req.email,
            password = hashed,
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

    @Cacheable("getAllUsers")
    override fun getAllUsers(): List<ResGetUserDto>{
        return masterUserRepository.findAll().map { user ->
            ResGetUserDto(
                userId = user.id!!,
                email = user.email,
                fullName = user.fullName,
                roleName = user.role?.name,
                age = user.age
            )
        }
    }

    @Cacheable("getUserById", key = "#userId")
    override fun getUserById(userId: Int): ResGetUserDetailDto {
        val user = masterUserRepository.findById(userId).orElseThrow {
            throw DataNotFoundException("User with id $userId not found!")
        }

        return ResGetUserDetailDto(
            userId = user.id!!,
            fullName = user.fullName,
            email = user.email,
            age = user.age,
            roleName = user.role!!.name
        )
    }

    //MENGHAPUS DATA REDIS
    @Caching(evict = [
        CacheEvict("getUserById", key = "#userId"),
        CacheEvict("getAllUsers", allEntries = true)
    ])
    override fun updateUser(req: ReqUpdateUserDto, userId: Int): ResGetUserDto {
        //GET USER BY ID
        val user = masterUserRepository.findById(userId).orElseThrow {
            println("User with id $userId not found!!!")
            GeneralException(HttpStatus.NOT_FOUND, "User with id $userId not found!!!")
        }

        val hashed = passwordEncoder.encode(req.password)
        //UPDATE ENTITY
        user.email = req.email
        user.password = hashed
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

    @Caching(evict = [
        CacheEvict("getUserById", key = "#userId"),
        CacheEvict("getAllUsers", allEntries = true)
    ])
    override fun softDeleteUser(userId: Int): ResUserIdDto {
        //find user by id di db (query db)
        val user = masterUserRepository.findById(userId).orElseThrow {
            GeneralException(HttpStatus.NOT_FOUND, "User with id $userId not found!!!")
        } // -> local variabel di springboot
        //set data is_delete = false
        user.isDelete = true
        user.deletedAt = Timestamp(System.currentTimeMillis())
        user.deletedBy = user.id

        //save updated data user di db
        masterUserRepository.save(user)
        //send message to Kafka
        kafkaProducer.sendMessage("BATCH16_DELETE_USER_PRODUCT", user.id!!.toString())
        //return
        return ResUserIdDto(userId)
    }
}