package com.batch16.usermanagementservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class UsermanagementserviceApplication

fun main(args: Array<String>) {
	runApplication<UsermanagementserviceApplication>(*args)
}
