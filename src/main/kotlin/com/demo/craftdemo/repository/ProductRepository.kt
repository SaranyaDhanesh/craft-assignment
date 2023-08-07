package com.demo.craftdemo.repository

import com.demo.craftdemo.domain.Product
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount
import java.util.UUID

@EnableScan
@EnableScanCount
interface ProductRepository : DynamoDBCrudRepository<Product, UUID>{
}