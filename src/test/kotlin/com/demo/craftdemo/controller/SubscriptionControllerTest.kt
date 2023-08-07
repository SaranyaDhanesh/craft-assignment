package com.demo.craftdemo.controller

import com.demo.craftdemo.model.SubscribeProductRequest
import com.demo.craftdemo.service.SubscriptionService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.springframework.http.HttpStatus
import java.util.*

class SubscriptionControllerTest : DescribeSpec({

    lateinit var subscriptionController: SubscriptionController
    lateinit var mockSubscriptionService: SubscriptionService


    beforeTest {
        mockSubscriptionService = mockk()
        subscriptionController = SubscriptionController(
                subscriptionService = mockSubscriptionService
        )
    }


    describe("subscribeProduct"){

        it("should subscribe product for given user"){

            val subscribeProductRequest = SubscribeProductRequest(
                    userId = UUID.randomUUID(),
                    productIds = listOf(UUID.randomUUID())
            )

            every {
                mockSubscriptionService.subscribeProducts(subscribeProductRequest)
            } just runs

            val response = subscriptionController.subscribeProduct(subscribeProductRequest)

            response.statusCode.shouldBe(HttpStatus.OK)
        }
    }


})
