package com.demo.craftdemo.service

import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.Exception.IllegalStateException
import com.demo.craftdemo.domain.Product
import com.demo.craftdemo.domain.User
import com.demo.craftdemo.model.*
import com.demo.craftdemo.repository.ProductRepository
import com.demo.craftdemo.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(createUserRequest: CreateUserRequest): CreateUserResponse{
        if(createUserRequest.userName.isEmpty()){
            throw BadRequestException("User name should not be empty")
        }

        val savedProduct = runCatching {
            val user = User(
                    userName = createUserRequest.userName
            )
            userRepository.save(user)

        }.onFailure {
            logger.error("Product save failed")
        }.getOrThrow()

        return CreateUserResponse(
                userId = savedProduct.userId,
                userName = savedProduct.userName
        )

    }

    companion object {
        val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }
    }
}