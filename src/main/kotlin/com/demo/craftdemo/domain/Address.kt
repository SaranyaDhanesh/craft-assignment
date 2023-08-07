package com.demo.craftdemo.domain

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@DynamoDBDocument
data class Address(

    @DynamoDBAttribute(attributeName = "line_one")
    var lineOne: String ? = null,

    @DynamoDBAttribute(attributeName = "line_two")
    var lineTwo: String? = null,

    @DynamoDBAttribute(attributeName = "city")
    var city: String? = null,

    @DynamoDBAttribute(attributeName = "state")
    var state: String? = null,

    @DynamoDBAttribute(attributeName = "zip")
    var zip: String? = null,

    @DynamoDBAttribute(attributeName = "country")
    var country: String? = null
)
