package com.demo.craftdemo.controller

import com.demo.craftdemo.model.CreateProfileRequest
import com.demo.craftdemo.model.CreateProfileResponse
import com.demo.craftdemo.model.UserProfileResponse
import com.demo.craftdemo.service.UserProfileService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import java.util.*

class UserProfileControllerTest : DescribeSpec({

    lateinit var userProfileController: UserProfileController
    lateinit var mockUserProfileService: UserProfileService

    beforeTest {
        mockUserProfileService = mockk()
        userProfileController = UserProfileController(userProfileService = mockUserProfileService)
    }

    describe("createUserProfile"){
        it("should create new user profile"){

            val mockCreateProfileRequest = mockk<CreateProfileRequest>()
            val mockCreateProfileResponse = mockk<CreateProfileResponse>()

            every {
                mockUserProfileService.createProfile(mockCreateProfileRequest)
            } returns mockCreateProfileResponse

            val actualResponse = userProfileController.createUserProfile(mockCreateProfileRequest)

            actualResponse.statusCode.shouldBe(HttpStatus.CREATED)
            actualResponse.body.shouldBe(mockCreateProfileResponse)
        }
    }

    describe("grtUserProfile"){
        it("should get created user profile"){

            val profileId = UUID.randomUUID().toString()
            val mockGetProfileResponse = mockk<UserProfileResponse>()

            every {
                mockUserProfileService.getUserProfile(UUID.fromString(profileId))
            } returns mockGetProfileResponse

            val actualResponse = userProfileController.getUserProfileDetails(profileId)

            actualResponse.statusCode.shouldBe(HttpStatus.OK)
            actualResponse.body.shouldBe(mockGetProfileResponse)
        }
    }

    describe("updateUserProfile"){
        it("should update user profile"){

            val profileId = UUID.randomUUID()
            val mockCreateProfileRequest = mockk<CreateProfileRequest>()
            val mockCreateProfileResponse = mockk<CreateProfileResponse>()

            every {
                mockUserProfileService.updateUserProfile(mockCreateProfileRequest, profileId)
            } returns mockCreateProfileResponse

            val actualResponse = userProfileController.updateUserProfile(profileId, mockCreateProfileRequest)

            actualResponse.statusCode.shouldBe(HttpStatus.OK)
            actualResponse.body.shouldBe(mockCreateProfileResponse)
        }
    }




})
