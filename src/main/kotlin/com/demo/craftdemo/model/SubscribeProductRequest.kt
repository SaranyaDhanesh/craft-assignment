package com.demo.craftdemo.model

import java.util.UUID

data class SubscribeProductRequest(

        val productIds: List<UUID>,

        val userId: UUID
)
