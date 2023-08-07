package com.demo.craftdemo.repository

import com.demo.craftdemo.domain.Product
import com.demo.craftdemo.domain.User
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount
import java.util.UUID

@EnableScan
@EnableScanCount
interface UserRepository : DynamoDBCrudRepository<User, UUID>{
}