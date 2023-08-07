package com.demo.craftdemo.model

import java.util.UUID

data class AddProductResponse(
        val productId: UUID? = null,

        val productName: String? = null
)
