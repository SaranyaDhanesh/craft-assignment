package com.demo.craftdemo.model

import com.craftdemo.productsubscription.model.ValidateProfileRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import java.util.UUID

data class CreateProfileRequest(

        val userId: UUID,

        val companyName: String,

        val legalName: String,

        val companyAddress: Address,

        val legalAddress: Address,

        val taxIdentifier: String,

        val email: String,

        val website: String,


) {
    fun toValidateProfileRequest() = ValidateProfileRequest(
        userId = userId,
        companyName = companyName,
        legalName = legalName,
        taxIdentifier = taxIdentifier,
        email = email,
        website = website,
        companyAddress = companyAddress.toValidateAddressRequest(),
        legalAddress = legalAddress.toValidateAddressRequest()
    )
}
