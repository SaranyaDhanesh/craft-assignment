package com.demo.craftdemo.controller

import com.demo.craftdemo.model.*
import com.demo.craftdemo.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService

) {
    @PostMapping("api/v1/product")
    fun addProduct(
            @RequestBody addProductRequest: AddProductRequest
    ): ResponseEntity<AddProductResponse> {

        return ResponseEntity( productService.addProduct(addProductRequest), HttpStatus.CREATED)
    }

}