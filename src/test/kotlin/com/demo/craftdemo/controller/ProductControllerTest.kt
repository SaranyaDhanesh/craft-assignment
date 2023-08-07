package com.demo.craftdemo.controller

import com.demo.craftdemo.model.AddProductRequest
import com.demo.craftdemo.model.AddProductResponse
import com.demo.craftdemo.service.ProductService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import java.util.*

class ProductControllerTest : DescribeSpec({

    lateinit var productController: ProductController
    lateinit var mockProductService: ProductService

    beforeTest{
        mockProductService = mockk()
        productController = ProductController(productService = mockProductService)
    }

    describe("addProduct"){

        it("should add product"){
            val addProductRequest = AddProductRequest(
                    productName = "Quickbooks"
            )
            val addProductResponse = AddProductResponse(
                    productId = UUID.randomUUID(),
                    productName = "Quickbooks"
            )

            every {
                mockProductService.addProduct(addProductRequest)
            } returns addProductResponse

            val response = productController.addProduct(addProductRequest)

            response.statusCode.shouldBe(HttpStatus.CREATED)
            response.body.shouldBe(addProductResponse)


        }
    }


})
