package com.demo.craftdemo.service

import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.domain.Product
import com.demo.craftdemo.domain.Subscription
import com.demo.craftdemo.domain.User
import com.demo.craftdemo.model.SubscribeProductRequest
import com.demo.craftdemo.repository.ProductRepository
import com.demo.craftdemo.repository.SubscriptionRepository
import com.demo.craftdemo.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.mockito.Mockito.mock
import java.util.*
import kotlin.jvm.optionals.getOrNull

class SubscriptionServiceTest : DescribeSpec({

    lateinit var mockProductRepository: ProductRepository
    lateinit var mockUserRepository: UserRepository
    lateinit var mockSubscriptionRepository: SubscriptionRepository
    lateinit var subscriptionService: SubscriptionService

    beforeTest {
        mockProductRepository = mockk()
        mockUserRepository = mockk()
        mockSubscriptionRepository = mockk()
        subscriptionService = SubscriptionService(
                userRepository = mockUserRepository,
                productRepository = mockProductRepository,
                subscriptionRepository = mockSubscriptionRepository
        )
    }

    describe("subscribeProducts"){

        it("should subscribe for given products"){

            val userId = UUID.randomUUID()
            val firstProductId = UUID.randomUUID()
            val secondProductId = UUID.randomUUID()
            val subscribeProductRequest = SubscribeProductRequest(
                    userId = userId,
                    productIds = listOf(firstProductId, secondProductId)
            )

            val user = User(userId, "username")
            val firstProduct = Product(firstProductId, "testProductOne")
            val secondProduct = Product(secondProductId, "testProductTwo")

            every { mockUserRepository.findById(userId) } returns Optional.of(user)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.of(secondProduct)

            every { mockSubscriptionRepository.save(any()) } returns mock()

            subscriptionService.subscribeProducts(subscribeProductRequest)

            verify(exactly = 2) {
                mockSubscriptionRepository.save(any())
            }
        }

        it("should throw exception when productIds are empty") {
            val userId = UUID.randomUUID()
            val subscribeProductRequest = SubscribeProductRequest(
                    userId = userId,
                    productIds = listOf()
            )

            val expectedException = BadRequestException("Product Id's should not be empty")

            val actualException = shouldThrow<BadRequestException> {
                subscriptionService.subscribeProducts(subscribeProductRequest)
            }

            actualException.shouldBe(expectedException)

        }

        it("should throw exception when product is null") {
            val userId = UUID.randomUUID()
            val firstProductId = UUID.randomUUID()
            val subscribeProductRequest = SubscribeProductRequest(
                    userId = userId,
                    productIds = listOf(firstProductId)
            )

            val user = User(userId, "username")

            every { mockUserRepository.findById(userId) } returns Optional.of(user)
            every { mockProductRepository.findById(firstProductId) } returns Optional.empty()

            val expectedException = BadRequestException("Product not present with give product id")

            val actualException = shouldThrow<BadRequestException> {
                subscriptionService.subscribeProducts(subscribeProductRequest)
            }

            actualException.shouldBe(expectedException)

        }

        it("should throw exception when user is null") {
            val userId = UUID.randomUUID()
            val firstProductId = UUID.randomUUID()
            val subscribeProductRequest = SubscribeProductRequest(
                    userId = userId,
                    productIds = listOf(firstProductId)
            )

            every { mockUserRepository.findById(userId) } returns Optional.empty()

            val expectedException = BadRequestException("User not present with give subscriber id")

            val actualException = shouldThrow<BadRequestException> {
                subscriptionService.subscribeProducts(subscribeProductRequest)
            }

            actualException.shouldBe(expectedException)

        }

        it("should throw exception when subscription save failsl") {
            val userId = UUID.randomUUID()
            val firstProductId = UUID.randomUUID()
            val subscribeProductRequest = SubscribeProductRequest(
                    userId = userId,
                    productIds = listOf(firstProductId)
            )

            val user = User(userId, "username")
            val firstProduct = Product(firstProductId, "testProductOne")

            mockkObject(SubscriptionService)
            every { mockUserRepository.findById(userId) } returns Optional.of(user)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)

            val expectedException = Exception("Failed")
            every { mockSubscriptionRepository.save(any()) } throws  expectedException
            every {
                SubscriptionService.logger.error(any())
            } just runs


            val actualException = shouldThrow<Exception> {
                subscriptionService.subscribeProducts(subscribeProductRequest)
            }

            actualException.shouldBe(expectedException)
            verify {
                SubscriptionService.logger.error("Product Subscription failed")
            }

        }
    }

    describe("logger"){

        it("should have non null logger"){
            mockkObject(SubscriptionService)
            SubscriptionService.logger.shouldNotBeNull()
        }
    }


})
