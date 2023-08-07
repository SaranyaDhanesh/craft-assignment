package com.demo.craftdemo.controller

import com.demo.craftdemo.model.SubscribeProductRequest
import com.demo.craftdemo.service.SubscriptionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubscriptionController(
    private val subscriptionService: SubscriptionService

) {
    @PostMapping("api/v1/product/subscribe")
    fun subscribeProduct(
            @RequestBody subscribeProductRequest: SubscribeProductRequest
    ): ResponseEntity<Unit> {
        subscriptionService.subscribeProducts(subscribeProductRequest)

        return ResponseEntity(null, HttpStatus.OK)
    }

}