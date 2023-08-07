package com.demo.craftdemo.controller

import com.demo.craftdemo.model.*
import com.demo.craftdemo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService

) {
    @PostMapping("api/v1/user")
    fun createUser(
            @RequestBody createUserRequest: CreateUserRequest
    ): ResponseEntity<CreateUserResponse> {

        return ResponseEntity( userService.createUser(createUserRequest), HttpStatus.CREATED)
    }

}