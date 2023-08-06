package com.demo.craftdemo.repository

import com.demo.craftdemo.domain.Subscriber
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount
import java.util.UUID

@EnableScan
@EnableScanCount
interface SubscribeRepository : DynamoDBCrudRepository<Subscriber, UUID>{
}