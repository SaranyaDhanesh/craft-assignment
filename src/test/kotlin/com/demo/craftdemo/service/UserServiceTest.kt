package com.demo.craftdemo.service

import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.domain.User
import com.demo.craftdemo.model.CreateUserRequest
import com.demo.craftdemo.model.CreateUserResponse
import com.demo.craftdemo.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.util.*

class UserServiceTest : DescribeSpec({

    lateinit var mockUserRepository: UserRepository
    lateinit var userService: UserService

    beforeTest {
        mockUserRepository = mockk()
        userService = UserService(userRepository = mockUserRepository)
        mockkObject(UserService)

    }

    describe("createUser"){

        it("should create new user"){
            val userName = "test_user_name"
            val userId = UUID.randomUUID()
            val createUserRequest = CreateUserRequest(
                    userName = userName
            )
            val expectedResponse = CreateUserResponse(
                    userId = userId,
                    userName = userName
            )
            val user = User(
                    userId = userId,
                    userName = userName
            )

            every { mockUserRepository.save(any()) } returns  user

            val actualResponse = userService.createUser(createUserRequest)

            actualResponse.shouldBe(expectedResponse)
            verify(exactly = 1) { mockUserRepository.save(any()) }
        }

        it("should throw exception when userName is empty") {
            val userName = ""
            val createUserRequest = CreateUserRequest(userName = userName)

            val expectedException = BadRequestException("User name should not be empty")

            val actualException = shouldThrow<BadRequestException> {
                userService.createUser(createUserRequest)
            }

            actualException.shouldBe(expectedException)

        }

        it("should throw exception when user save fails") {
            val userName = "test_user_name"
            val createUserRequest = CreateUserRequest(userName = userName)

            val expectedException = Exception("Failed")
            every {
                mockUserRepository.save(any())
            } throws expectedException
            every { UserService.logger.error(any()) } just runs

            val actualException = shouldThrow<Exception> {
                userService.createUser(createUserRequest)
            }

            actualException.shouldBe(expectedException)
            verify {
                UserService.logger.error(any())
            }

        }

    }

    describe("logger"){
        it("should have non null logger"){
            mockkObject(UserService)
            UserService.logger.shouldNotBeNull()
        }
    }


})
