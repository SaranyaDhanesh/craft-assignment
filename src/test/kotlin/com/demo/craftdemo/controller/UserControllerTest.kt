package com.demo.craftdemo.controller

import com.demo.craftdemo.model.CreateUserRequest
import com.demo.craftdemo.model.CreateUserResponse
import com.demo.craftdemo.service.UserService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import java.util.*

class UserControllerTest : DescribeSpec({

    lateinit var userController: UserController
    lateinit var mockUserService: UserService

    beforeTest {
        mockUserService = mockk()
        userController = UserController(userService = mockUserService)
    }

    describe("createUser"){

        it("should create new user") {

            val createUserRequest = CreateUserRequest(
                    userName = "test_name"
            )
            val createUserResponse = CreateUserResponse(
                    userId = UUID.randomUUID()
            )

            every {
                mockUserService.createUser(createUserRequest)
            } returns createUserResponse

            val response = userController.createUser(createUserRequest)

            response.statusCode.shouldBe(HttpStatus.CREATED)
            response.body.shouldBe(createUserResponse)
        }

    }


})
