package com.demo.craftdemo.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import   com.craftdemo.productsubscription.model.Address as AddressForValidation

data class Address(

        @NotNull(message = "lineOne should not be null")
        @Size(max = 100)
        val lineOne: String,

        val lineTwo: String? = null,

        @Size(max = 50)
        val city: String,

        @Size(max = 50)
        val state: String,

        @Size(max = 6)
        val zip: String,

        @Size(max = 50)
        val country: String
){
        fun toValidateAddressRequest() =
                AddressForValidation(
                        lineOne = lineOne,
                        lineTwo = lineTwo,
                        city = city,
                        state = state,
                        zip = zip,
                        country = country
                )

}
