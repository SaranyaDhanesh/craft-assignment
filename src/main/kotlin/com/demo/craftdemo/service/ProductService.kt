package com.demo.craftdemo.service

import com.demo.craftdemo.Exception.BadRequestException
import com.demo.craftdemo.Exception.IllegalStateException
import com.demo.craftdemo.domain.Product
import com.demo.craftdemo.model.*
import com.demo.craftdemo.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {


    fun addProduct(addProductRequest: AddProductRequest): AddProductResponse{
        if(addProductRequest.productName.isEmpty()){
            throw BadRequestException("Product name should not be empty")
        }

        val savedProduct = runCatching {
            val product = Product(
                    productName = addProductRequest.productName
            )
            productRepository.save(product)

        }.onFailure {
            logger.error("Product save failed")
        }.getOrThrow()

        return AddProductResponse(
                productId = savedProduct.productId,
                productName = savedProduct.productName
        )


    }

    companion object {
        val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }
    }
}