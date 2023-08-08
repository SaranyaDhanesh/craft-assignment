package com.demo.craftdemo.Exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun onMethodArgumentTypeMismatchException(
            exception: MethodArgumentTypeMismatchException
    ): ResponseEntity<ErrorResponse> {
        logger.info("On http message not readable exception")
        val errorResponse = ErrorResponse(
                field = exception.name,
                message = String.format("%s is not in valid format", exception.name)
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)

    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun onHttpMessageNotReadableException(
            exception: HttpMessageNotReadableException
    ): ResponseEntity<ErrorResponse> {
        logger.info("On http message not readable exception")
        val errorResponse = when (exception.rootCause){
            is MissingKotlinParameterException -> {
                val missingKotlinParameterException = exception.rootCause as MissingKotlinParameterException
                ErrorResponse(
                        field = missingKotlinParameterException.parameter.name,
                        message = "Field is missing"
                )
            }
            else -> {
                ErrorResponse(
                        message = "Unable to parse the request payload"
                )
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)

    }

    @ExceptionHandler(BadRequestException::class)
    fun onBadRequestException(
            exception: BadRequestException
    ): ResponseEntity<ErrorResponse> {
        logger.info("On Bad request exception")
        val errorResponse = ErrorResponse(
                message = exception.message
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)

    }

    @ExceptionHandler(IllegalStateException::class)
    fun onIllegalStateException(
            exception: IllegalStateException
    ): ResponseEntity<ErrorResponse> {
        logger.info("On Bad request exception")
        val errorResponse = ErrorResponse(
                message = exception.message
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)

    }

    companion object {
        val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }
    }
}