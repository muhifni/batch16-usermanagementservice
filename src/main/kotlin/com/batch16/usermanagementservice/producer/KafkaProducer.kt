package com.batch16.usermanagementservice.producer

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer<T>(
    private val kafkaTemplate: KafkaTemplate<String, T>
) {
    val log = LoggerFactory.getLogger(this::class.java)
    fun sendMessage(topic: String, message: T) {
        log.info("Sending message $message")
        kafkaTemplate.send(topic, message)
    }
}