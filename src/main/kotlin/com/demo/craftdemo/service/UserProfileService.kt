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

            userProfileRepository.findByUserId(createProfileRequest.userId)?.let {
                throw BadRequestException("Profile already present for given user")
            }

            validateUser(createProfileRequest)
            validateSubscribedProducts(createProfileRequest)

            val subscriber = DomainModelMapper.convertToUserProfileEntity(createProfileRequest)
            userProfileRepository.save(subscriber)

        }.onFailure {
            logger.error("User profile creation failed")
        }.getOrThrow()

        savedUserProfile.userProfileId?.let {
            return CreateProfileResponse(userProfileId = it)
        } ?: throw IllegalStateException("User profile id is not present")

    }

    fun updateUserProfile(createProfileRequest: CreateProfileRequest): CreateProfileResponse {
         runCatching {

            val userProfileId = createProfileRequest.userProfileId
                    ?: throw BadRequestException("User profile id should be present")
            val userProfile = userProfileRepository.findById(userProfileId).getOrNull()
                ?: throw BadRequestException("User profile not found")

            validateSubscribedProducts(createProfileRequest)

            val subscriber = DomainModelMapper.updateUserProfileEntity(createProfileRequest, userProfile)
            userProfileRepository.save(subscriber)

        }.onFailure {
            logger.error("User profile update failed")
        }.getOrThrow()

        return CreateProfileResponse(userProfileId = createProfileRequest.userProfileId!!)

    }

    private fun validateSubscribedProducts(createProfileRequest: CreateProfileRequest) {
        val subscribedProducts = subscriptionRepository.findByUserId(createProfileRequest.userId)
        if (subscribedProducts.isEmpty()) {
            logger.error("User dont have any subscription")
            throw BadRequestException("User dont have any subscription")
        } else {
            val products = subscribedProducts.map { subscribedProduct ->
                productRepository.findById(subscribedProduct.productId!!).getOrElse {
                    throw BadRequestException("Product with given product id not found")
                }
            }
            validateProfileBySubscribedProducts(createProfileRequest, products)
        }
    }

    private fun validateUser(createProfileRequest: CreateProfileRequest) {
        val user = userRepository.findById(createProfileRequest.userId)
        user.getOrNull() ?: throw BadRequestException("User with given userId not found")
    }


    fun getUserProfile(userProfileId: UUID): UserProfileResponse {
        val userProfile = userProfileRepository.findById(userProfileId)

        if(userProfile.getOrNull() == null){
            throw BadRequestException("Profile not found with given profile id")
        }

        return DomainModelMapper.convertToProfileResponse(userProfile.get())

    }

    private fun validateProfileBySubscribedProducts(createProfileRequest: CreateProfileRequest, products: List<Product>){
       val productNames = products.map { it.productName!! }
       val validateProfileRequest: ValidateProfileRequest = createProfileRequest.toValidateProfileRequest()
       runCatching {
           productSubscriptionAdapter.validateProfileByProduct(validateProfileRequest, productNames)
       }.onFailure {
           logger.error("Product validation request failed")
       }.getOrThrow()

    }

    companion object {
        val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }
    }
}