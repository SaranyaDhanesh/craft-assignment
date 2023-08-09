package com.demo.craftdemo.repository

import com.demo.craftdemo.domain.User
import com.demo.craftdemo.domain.UserProfile
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount
import java.util.UUID

@EnableScan
@EnableScanCount
interface UserProfileRepository : DynamoDBCrudRepository<UserProfile, UUID>{

    fun findByUserId(userId: UUID): UserProfile?
}