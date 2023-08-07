package com.demo.craftdemo.mapper

import com.demo.craftdemo.domain.UserProfile
import com.demo.craftdemo.model.Address
import com.demo.craftdemo.model.AddressResponse
import com.demo.craftdemo.model.CreateProfileRequest
import com.demo.craftdemo.model.UserProfileResponse
import  com.demo.craftdemo.domain.Address as AddressEntity

object DomainModelMapper {

    fun convertToUserProfileEntity(createProfileRequest: CreateProfileRequest) =
         UserProfile(
             companyName = createProfileRequest.companyName,
             legalName = createProfileRequest.legalName,
             taxIdentifier = createProfileRequest.taxIdentifier,
             website = createProfileRequest.website,
             email = createProfileRequest.email,
             legalAddress = convertToAddress(createProfileRequest.legalAddress),
             businessAddress = convertToAddress(createProfileRequest.companyAddress),
             userId = createProfileRequest.userId
         )

    private fun convertToAddress(address: Address): AddressEntity =
        AddressEntity(
            lineOne = address.lineOne,
            lineTwo = address.lineTwo,
            city = address.city,
            state = address.state,
            zip = address.zip,
            country = address.country
        )

    fun updateUserProfileEntity(
        createProfileRequest: CreateProfileRequest,
        userProfile: UserProfile
    ): UserProfile {
        userProfile.setLegalName(createProfileRequest.legalName)
        userProfile.setCompanyName(createProfileRequest.companyName)
        userProfile.setLegalAddress(convertToAddress(createProfileRequest.legalAddress))
        userProfile.setBusinessAddress(convertToAddress(createProfileRequest.companyAddress))
        userProfile.setTaxIdentifier(createProfileRequest.taxIdentifier)
        userProfile.setEmail(createProfileRequest.email)
        userProfile.setEmail(createProfileRequest.email)
        userProfile.setWebsite(createProfileRequest.website)

        return userProfile

    }

    fun convertToProfileResponse(user: UserProfile) =
            UserProfileResponse(
                    subscriberId = user.userProfileId,
                    companyName = user.getCompanyName(),
                    legalName = user.getLegalName(),
                    website = user.getWebsite(),
                    email = user.getEmail(),
                    companyAddress = convertToAddressModel(user.getBusinessAddress()),
                    legalAddress = convertToAddressModel(user.getLegalAddress()),
                    taxIdentifier = user.getTaxIdentifier()

            )

    private fun convertToAddressModel(addressEntity: AddressEntity?) =
            addressEntity?.let {
                AddressResponse(
                    lineOne = addressEntity.lineOne,
                    lineTwo = addressEntity.lineTwo,
                    city = addressEntity.city,
                    state = addressEntity.state,
                    zip = addressEntity.zip,
                    country = addressEntity.country
                )
            }

}