package com.demo.craftdemo.controller

import com.demo.craftdemo.model.CreateProfileRequest
import com.demo.craftdemo.model.CreateProfileResponse
import com.demo.craftdemo.model.UserProfileResponse
import com.demo.craftdemo.service.UserProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class UserProfileController(
    private val userProfileService: UserProfileService
) {

    @PostMapping("api/v1/user/profile")
    fun createUserProfile(
        @RequestBody createProfileRequest: CreateProfileRequest
    ): ResponseEntity<CreateProfileResponse>{

        return ResponseEntity(userProfileService.createProfile(createProfileRequest), HttpStatus.CREATED)

    }

    @GetMapping("api/v1/user/profile")
    fun getUserProfileDetails(
            @RequestParam id: String
    ): ResponseEntity<UserProfileResponse>{

        return ResponseEntity(userProfileService.getUserProfile(userProfileId = UUID.fromString(id)), HttpStatus.OK)

    }

    @PutMapping("api/v1/user/profile")
    fun updateUserProfile(
            @RequestParam id: UUID,
            @RequestBody createProfileRequest: CreateProfileRequest
    ): ResponseEntity<CreateProfileResponse>{

        return ResponseEntity(userProfileService.updateUserProfile(createProfileRequest, id), HttpStatus.OK)

    }

}