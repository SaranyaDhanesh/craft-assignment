package com.demo.craftdemo.repository

import com.demo.craftdemo.domain.Subscription
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount
import java.util.UUID

@EnableScan
@EnableScanCount
interface SubscriptionRepository : DynamoDBCrudRepository<Subscription, UUID>{

    fun findByUserId(userId: UUID): List<Subscription>
}