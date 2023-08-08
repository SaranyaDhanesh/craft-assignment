package com.demo.craftdemo.model

import java.util.UUID

data class UserProfileResponse(

        val userProfileId: UUID?,

        val companyName: String?,

        val legalName: String?,

        val companyAddress: AddressResponse?,

        val legalAddress: AddressResponse?,

        val taxIdentifier: String?,

        val email: String?,

        val website: String?

)
