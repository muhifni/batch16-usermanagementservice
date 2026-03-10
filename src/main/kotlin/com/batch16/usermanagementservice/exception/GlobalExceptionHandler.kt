package com.batch16.usermanagementservice.exception

import com.batch16.usermanagementservice.domain.dto.BaseResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(
        ex: RuntimeException
    ): ResponseEntity<BaseResponse<String>>{
        ex.printStackTrace() //Error muncul di log
        return ResponseEntity(
            BaseResponse(
                message = "Bad Request",
                data = ex.message,
                success = false,
                status = 400
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataNotFoundException::class)
    fun handleDataNotFoundException(
        exception: RuntimeException
    ): ResponseEntity<BaseResponse<Any?>>{
        exception.printStackTrace()
        return ResponseEntity.badRequest().body(
            BaseResponse<Any?>(
                message = exception.message.toString(),
                status = HttpStatus.NOT_FOUND.value(),
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<BaseResponse<Any?>>{
        exception.printStackTrace()
        var errors = mutableListOf<String>()
        exception.bindingResult.fieldErrors.forEach {
            errors.add(it.defaultMessage!!)
        }
        return ResponseEntity.badRequest().body(
            BaseResponse<Any?>(
                "Field Request Not Valid",
                HttpStatus.BAD_REQUEST.value(),
                false,
                errors
            )
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException
    ): ResponseEntity<BaseResponse<Any?>> {
        return ResponseEntity.badRequest().body(
            BaseResponse(
                message = "Request body is required",
                status = 400,
                success = false
            )
        )
    }

}