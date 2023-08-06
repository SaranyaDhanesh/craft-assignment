package com.demo.craftdemo

import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [ "com.demo.craftdemo", "com.demo.craftdemo.domain"])
@EnableScan
class CraftDemoApplication

fun main(args: Array<String>) {
    runApplication<CraftDemoApplication>(*args)
}
