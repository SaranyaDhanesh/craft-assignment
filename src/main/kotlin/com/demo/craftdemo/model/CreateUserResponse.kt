package com.demo.craftdemo.model

import java.util.UUID

data class CreateUserResponse(
        val userId: UUID? = null,

        val userName: String? = null
)
