package com.demo.craftdemo.service

import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.domain.Subscription
import com.demo.craftdemo.model.SubscribeProductRequest
import com.demo.craftdemo.repository.ProductRepository
import com.demo.craftdemo.repository.SubscriptionRepository
import com.demo.craftdemo.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class SubscriptionService(
        private val productRepository: ProductRepository,
        private val userRepository: UserRepository,
        private val subscriptionRepository: SubscriptionRepository
) {


    fun subscribeProducts(subscribeProductRequest: SubscribeProductRequest){
        if(subscribeProductRequest.productIds.isEmpty()){
            throw BadRequestException("Product Id's should not be empty")
        }

        runCatching {
            val user = userRepository.findById(subscribeProductRequest.userId)
            if(user.getOrNull() == null){
                throw BadRequestException("User not present with give subscriber id")
            }

            subscribeProductRequest.productIds.map {productId ->
                val product = productRepository.findById(productId)
                if(product.getOrNull() == null){
                    throw BadRequestException("Product not present with give product id")
                }
                val subscription = Subscription(
                        userId = user.get().userId,
                        productId = product.get().productId
                )

                subscriptionRepository.save(subscription)

            }

        }.onFailure {
            logger.error("Product Subscription failed")
        }.getOrThrow()
    }

    companion object {
        val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }
    }
}