package com.demo.craftdemo.service

import com.craftdemo.productsubscription.interfaces.ProductSubscriptionAdapter
import com.craftdemo.productsubscription.model.ValidateProfileRequest
import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.Exception.IllegalStateException
import com.demo.craftdemo.domain.Product
import com.demo.craftdemo.mapper.DomainModelMapper
import com.demo.craftdemo.model.CreateProfileRequest
import com.demo.craftdemo.model.CreateProfileResponse
import com.demo.craftdemo.model.UserProfileResponse
import com.demo.craftdemo.repository.ProductRepository
import com.demo.craftdemo.repository.SubscriptionRepository
import com.demo.craftdemo.repository.UserProfileRepository
import com.demo.craftdemo.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val userRepository: UserRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val productRepository: ProductRepository,
    private val productSubscriptionAdapter: ProductSubscriptionAdapter
) {

    fun createProfile(createProfileRequest: CreateProfileRequest): CreateProfileResponse {
        val savedUserProfile = runCatching {

            validateUserAndSubscribedProducts(createProfileRequest)

            val subscriber = DomainModelMapper.convertToUserProfileEntity(createProfileRequest)
            userProfileRepository.save(subscriber)

        }.onFailure {exception ->
            logger.info("Subscriber create profile failed")
            throw IllegalStateException(exception.message!!)
        }.getOrThrow()

        savedUserProfile.userProfileId?.let {
            return CreateProfileResponse(userProfileId = it)
        } ?: throw IllegalStateException("Subscriber user id is not present")

    }

    fun updateUserProfile(createProfileRequest: CreateProfileRequest, profileId: UUID): CreateProfileResponse {
        val updatedUserProfile = runCatching {

            val userProfile = userProfileRepository.findById(profileId).getOrNull()
                ?: throw BadRequestException("User profile not found")

            validateUserAndSubscribedProducts(createProfileRequest)

            val subscriber = DomainModelMapper.updateUserProfileEntity(createProfileRequest, userProfile)
            userProfileRepository.save(subscriber)

        }.onFailure {exception ->
            logger.info("Subscriber create profile failed")
            throw IllegalStateException(exception.message!!)
        }.getOrThrow()

        updatedUserProfile.userProfileId?.let {
            return CreateProfileResponse(userProfileId = it)
        } ?: throw IllegalStateException("Subscriber user id is not present")

    }

    private fun validateUserAndSubscribedProducts(createProfileRequest: CreateProfileRequest) {
        val user = userRepository.findById(createProfileRequest.userId)
        user.getOrNull() ?: throw BadRequestException("User with given userId not found")

        val subscribedProducts = subscriptionRepository.findByUserId(createProfileRequest.userId)
        if (subscribedProducts.isEmpty()) {
            logger.info("User dont have any subscription")
        } else {
            val products = subscribedProducts.map { subscribedProduct ->
                productRepository.findById(subscribedProduct.productId!!).getOrElse {
                    throw BadRequestException("Product with given product id not found")
                }
            }
            validateProfileBySubscribedProducts(createProfileRequest, products)
        }
    }


    fun getUserProfile(subscriberId: UUID): UserProfileResponse {
        val subscriber = userProfileRepository.findById(subscriberId)

        if(subscriber.getOrNull() == null){
            throw BadRequestException("Subscriber not found with given subscriber id")
        }

        return DomainModelMapper.convertToProfileResponse(subscriber.get())

    }

    private fun validateProfileBySubscribedProducts(createProfileRequest: CreateProfileRequest, products: List<Product>){
       val productNames = products.map { it.productName!! }
       val validateProfileRequest: ValidateProfileRequest = createProfileRequest.toValidateProfileRequest()
       runCatching {
           productSubscriptionAdapter.validateProfileByProduct(validateProfileRequest, productNames)
       }.onFailure {
           logger.info("Validation request failed")
       }.getOrThrow()

    }

    companion object {
        val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }
    }
}