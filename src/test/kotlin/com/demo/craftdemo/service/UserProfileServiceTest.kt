package com.demo.craftdemo.service

import com.craftdemo.productsubscription.interfaces.ProductSubscriptionAdapter
import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.Exception.IllegalStateException
import com.demo.craftdemo.domain.Product
import com.demo.craftdemo.domain.Subscription
import com.demo.craftdemo.domain.User
import com.demo.craftdemo.domain.UserProfile
import com.demo.craftdemo.model.Address
import com.demo.craftdemo.model.CreateProfileRequest
import com.demo.craftdemo.repository.ProductRepository
import com.demo.craftdemo.repository.SubscriptionRepository
import com.demo.craftdemo.repository.UserProfileRepository
import com.demo.craftdemo.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.mockito.Mockito.mock
import java.util.*

class UserProfileServiceTest : DescribeSpec({

    lateinit var mockProductRepository: ProductRepository
    lateinit var mockUserRepository: UserRepository
    lateinit var mockUserProfileRepository: UserProfileRepository
    lateinit var mockSubscriptionRepository: SubscriptionRepository
    lateinit var mockProductSubscriptionAdapter: ProductSubscriptionAdapter
    lateinit var userProfileService: UserProfileService

    beforeTest {
        mockProductRepository = mockk()
        mockSubscriptionRepository = mockk()
        mockUserRepository = mockk()
        mockUserProfileRepository = mockk()
        mockProductSubscriptionAdapter = mockk()
        userProfileService = UserProfileService(
            productRepository = mockProductRepository,
            userRepository = mockUserRepository,
            userProfileRepository = mockUserProfileRepository,
            subscriptionRepository = mockSubscriptionRepository,
            productSubscriptionAdapter = mockProductSubscriptionAdapter
        )
    }

    describe("createProfile"){

        val userId = UUID.randomUUID()
        val address = Address(
                lineOne = "testLineOne",
                lineTwo = "testLineTwo",
                city = "testCity",
                state = "testState",
                zip = "12345",
                country = "testCountry"
        )
        val createProfileRequest = CreateProfileRequest(
                userId = userId,
                companyName = "testCompanyName",
                legalName = "legalName",
                taxIdentifier = "XYZ",
                email = "test@gmail.com",
                companyAddress = address,
                legalAddress = address,
                website = "http://localhost:8080"
        )
        val user = User(
                userId = userId,
                userName = "test_name"
        )
        val firstProductId = UUID.randomUUID()
        val secondProductId = UUID.randomUUID()
        val firstProductName = "Quickbooks"
        val secondProductName = "Qb Payments"
        val firstProduct = Product(
                productId = firstProductId,
                productName = firstProductName
        )
        val secondProduct = Product(
                productId = secondProductId,
                productName = secondProductName
        )
        val firstSubscription = Subscription(
                userId = userId,
                productId = firstProductId
        )
        val secondSubscription = Subscription(
                userId = userId,
                productId = secondProductId
        )
        val userProfileId = UUID.randomUUID()

        it("should create user profile"){

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf(firstSubscription, secondSubscription)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.of(secondProduct)
            every {
                mockProductSubscriptionAdapter.validateProfileByProduct(
                        createProfileRequest.toValidateProfileRequest(),
                        listOf(firstProductName, secondProductName)
                    )
            } just runs
            every { mockUserProfileRepository.save(any()) } returns UserProfile(userProfileId = userProfileId)

            val actualResponse = userProfileService.createProfile(createProfileRequest)

            actualResponse.shouldNotBeNull()
            actualResponse.userProfileId.shouldNotBeNull()
        }

        it("should throw exception when user profile save fails"){

            val expectedException = IllegalStateException("Failed")

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf(firstSubscription, secondSubscription)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.of(secondProduct)
            every {
                mockProductSubscriptionAdapter.validateProfileByProduct(
                        createProfileRequest.toValidateProfileRequest(),
                        listOf(firstProductName, secondProductName)
                )
            } just runs
            every { mockUserProfileRepository.save(any()) } throws expectedException

            val actualException = shouldThrow<IllegalStateException> {
                userProfileService.createProfile(createProfileRequest)
            }

            actualException.shouldBe(expectedException)


        }

        it("should throw exception when user profile id not present"){

            val expectedException = IllegalStateException("User profile id is not present")

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf(firstSubscription, secondSubscription)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.of(secondProduct)
            every {
                mockProductSubscriptionAdapter.validateProfileByProduct(
                        createProfileRequest.toValidateProfileRequest(),
                        listOf(firstProductName, secondProductName)
                )
            } just runs
            every { mockUserProfileRepository.save(any()) } returns UserProfile()

            val actualException = shouldThrow<IllegalStateException> {
                userProfileService.createProfile(createProfileRequest)
            }

            actualException.shouldBe(expectedException)


        }

        it("should throw exception when user not found"){

            val expectedException = BadRequestException("User with given userId not found")

            every {
                mockUserRepository.findById(userId)
            } returns Optional.empty()

            val actualException = shouldThrow<BadRequestException> {
                userProfileService.createProfile(createProfileRequest)
            }

            actualException.shouldBe(expectedException)


        }

        it("should throw exception when subscribed products are empty"){

            val expectedException = BadRequestException("User dont have any subscription")

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf()

            val actualException = shouldThrow<BadRequestException> {
                userProfileService.createProfile(createProfileRequest)
            }

            actualException.shouldBe(expectedException)

        }

        it("should throw exception when product not found"){

            val expectedException = BadRequestException("Product with given product id not found")

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf(firstSubscription, secondSubscription)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.empty()
            every {
                mockProductSubscriptionAdapter.validateProfileByProduct(
                        createProfileRequest.toValidateProfileRequest(),
                        listOf(firstProductName, secondProductName)
                )
            } just runs

            val actualException = shouldThrow<BadRequestException> {
                userProfileService.createProfile(createProfileRequest)
            }

            actualException.shouldBe(expectedException)


        }

        it("should throw exception when product not found"){

            val expectedException = Exception("Failed")

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf(firstSubscription, secondSubscription)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.of(secondProduct)
            every {
                mockProductSubscriptionAdapter.validateProfileByProduct(
                        createProfileRequest.toValidateProfileRequest(),
                        listOf(firstProductName, secondProductName)
                )
            } throws expectedException

            val actualException = shouldThrow<Exception> {
                userProfileService.createProfile(createProfileRequest)
            }

            actualException.shouldBe(expectedException)

        }

    }

    describe("getUserProfile"){

        val address = com.demo.craftdemo.domain.Address(
                lineOne = "testLineOne",
                lineTwo = "testLineTwo",
                city = "testCity",
                state = "testState",
                zip = "12345",
                country = "testCountry"
        )

        val userProfile = UserProfile(
                userId = UUID.randomUUID(),
                companyName = "testCompanyName",
                legalName = "testLegalName",
                taxIdentifier = "XYZ",
                email = "test@gmail.com",
                businessAddress = address,
                legalAddress = address,
                website = "http://localhost:8080"
        )
        it("should return user profile details"){
            val userProfileId = UUID.randomUUID()

            every {
                mockUserProfileRepository.findById(userProfileId)
            } returns Optional.of(userProfile)

            val actualResponse = userProfileService.getUserProfile(userProfileId)

            actualResponse.companyName.shouldBe("testCompanyName")
            actualResponse.legalName.shouldBe("testLegalName")
            actualResponse.website.shouldBe("http://localhost:8080")
            actualResponse.email.shouldBe("test@gmail.com")
            actualResponse.companyAddress.shouldNotBeNull()
            actualResponse.legalAddress.shouldNotBeNull()
        }

        it("should throw exception when id is not valid"){
            val userProfileId = UUID.randomUUID()
            val expectedException = BadRequestException("Profile not found with given profile id")

            every {
                mockUserProfileRepository.findById(userProfileId)
            } returns Optional.empty()

            val actualException = shouldThrow<BadRequestException> { userProfileService.getUserProfile(userProfileId) }

            actualException.shouldBe(expectedException)

        }
    }

    describe("updateUserProfile"){

        val userId = UUID.randomUUID()
        val userProfileId = UUID.randomUUID()
        val address = Address(
                lineOne = "testLineOne",
                lineTwo = "testLineTwo",
                city = "testCity",
                state = "testState",
                zip = "12345",
                country = "testCountry"
        )
        val createProfileRequest = CreateProfileRequest(
                userId = userId,
                companyName = "updatedCompanyName",
                legalName = "updatedLegalName",
                taxIdentifier = "XYZ",
                email = "updatedtest@gmail.com",
                companyAddress = address,
                legalAddress = address,
                website = "http://localhost:8080"
        )
        val user = User(
                userId = userId,
                userName = "test_name"
        )
        val firstProductId = UUID.randomUUID()
        val secondProductId = UUID.randomUUID()
        val firstProductName = "Quickbooks"
        val secondProductName = "Qb Payments"
        val firstProduct = Product(
                productId = firstProductId,
                productName = firstProductName
        )
        val secondProduct = Product(
                productId = secondProductId,
                productName = secondProductName
        )
        val firstSubscription = Subscription(
                userId = userId,
                productId = firstProductId
        )
        val secondSubscription = Subscription(
                userId = userId,
                productId = secondProductId
        )

        val addressEntity = com.demo.craftdemo.domain.Address(
                lineOne = "testLineOne",
                lineTwo = "testLineTwo",
                city = "testCity",
                state = "testState",
                zip = "12345",
                country = "testCountry"
        )

        val userProfile = UserProfile(
                userId = UUID.randomUUID(),
                companyName = "testCompanyName",
                legalName = "testLegalName",
                taxIdentifier = "XYZ",
                email = "test@gmail.com",
                businessAddress = addressEntity,
                legalAddress = addressEntity,
                website = "http://localhost:8080"
        )

        it("should create user profile"){

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every { mockUserProfileRepository.findById(userProfileId) } returns Optional.of(userProfile)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf(firstSubscription, secondSubscription)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.of(secondProduct)
            every {
                mockProductSubscriptionAdapter.validateProfileByProduct(
                        createProfileRequest.toValidateProfileRequest(),
                        listOf(firstProductName, secondProductName)
                )
            } just runs
            every { mockUserProfileRepository.save(any()) } returns UserProfile(userProfileId = userProfileId)

            val actualResponse = userProfileService.updateUserProfile(createProfileRequest, userProfileId)

            actualResponse.shouldNotBeNull()
            actualResponse.userProfileId.shouldNotBeNull()
        }

        it("should throw exception when user profile save fails"){

            val expectedException = IllegalStateException("Failed")

            every {
                mockUserRepository.findById(userId)
            } returns Optional.of(user)
            every { mockUserProfileRepository.findById(userProfileId) } returns Optional.of(userProfile)
            every {
                mockSubscriptionRepository.findByUserId(userId)
            } returns listOf(firstSubscription, secondSubscription)
            every { mockProductRepository.findById(firstProductId) } returns Optional.of(firstProduct)
            every { mockProductRepository.findById(secondProductId) } returns Optional.of(secondProduct)
            every {
                mockProductSubscriptionAdapter.validateProfileByProduct(
                        createProfileRequest.toValidateProfileRequest(),
                        listOf(firstProductName, secondProductName)
                )
            } just runs
            every { mockUserProfileRepository.save(any()) } throws Exception("Failed")

            val actualException = shouldThrow<IllegalStateException> {
                userProfileService.updateUserProfile(createProfileRequest, userProfileId)
            }

            actualException.shouldBe(expectedException)

        }


        it("should throw exception when user profile not found"){

            val expectedException = IllegalStateException("User profile not found")

            every {
                mockUserProfileRepository.findById(userProfileId)
            } returns Optional.empty()

            val actualException = shouldThrow<IllegalStateException> {
                userProfileService.updateUserProfile(createProfileRequest, userProfileId)
            }

            actualException.shouldBe(expectedException)

        }

    }


})
