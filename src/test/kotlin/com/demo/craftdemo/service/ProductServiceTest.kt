package com.demo.craftdemo.service

import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.domain.Product
import com.demo.craftdemo.model.AddProductRequest
import com.demo.craftdemo.model.AddProductResponse
import com.demo.craftdemo.repository.ProductRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.util.*

class ProductServiceTest : DescribeSpec({

    lateinit var mockProductRepository: ProductRepository
    lateinit var productService: ProductService

    beforeTest {
        mockProductRepository = mockk()
        productService = ProductService(productRepository = mockProductRepository)
        mockkObject(ProductService)
        every {  ProductService.logger.error(any()) } just runs
    }

    describe("addProduct"){

        it("should add new product"){
            val productName = "quickbooks"
            val productId = UUID.randomUUID()
            val addProductRequest = AddProductRequest(
                    productName = productName
            )
            val product = Product(
                    productId =productId,
                    productName = productName
            )
            val expectedResponse = AddProductResponse(
                    productId = productId,
                    productName = productName
            )

            every {
                mockProductRepository.save(any())
            } returns product

            val actualResponse = productService.addProduct(addProductRequest)

            actualResponse.shouldBe(expectedResponse)
            verify(exactly = 1) {
                mockProductRepository.save(any())
            }
        }

        it("should throw exception if product name is empty"){
            val addProductRequest = AddProductRequest(
                    productName = ""
            )
            val expectedException = BadRequestException("Product name should not be empty")

            val actualException = shouldThrow<BadRequestException> {
                productService.addProduct(addProductRequest)
            }

            actualException.shouldBe(expectedException)
        }

        it("should throw exception if repository save fails"){
            val addProductRequest = AddProductRequest(
                    productName = "quickbooks"
            )
            val expectedException = Exception("Failed")

            every {
                mockProductRepository.save(any())
            } throws  expectedException
            every {  ProductService.logger.error(any()) } just runs

            val actualException = shouldThrow<Exception> {
                productService.addProduct(addProductRequest)
            }

            actualException.shouldBe(expectedException)
            verify(exactly = 1){
                mockProductRepository.save(any())
            }
            verify(exactly = 1) {
                ProductService.logger.error(any())
            }
        }
    }

    describe("Logger"){
        it("should have non null logger"){
            mockkObject(ProductService)
            ProductService.logger.shouldNotBeNull()
        }
    }


})
