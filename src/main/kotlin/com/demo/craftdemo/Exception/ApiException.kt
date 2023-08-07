package com.demo.craftdemo.Exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class BadRequestException(
        message: String,
        code: Int = HttpStatus.BAD_REQUEST.value()
): RuntimeException(message)


class IllegalStateException(
        message: String,
        code: Int = HttpStatus.BAD_REQUEST.value()
): RuntimeException(message)