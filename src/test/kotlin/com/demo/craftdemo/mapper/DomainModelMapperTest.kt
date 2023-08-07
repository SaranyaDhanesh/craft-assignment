package com.demo.craftdemo.mapper

import com.demo.craftdemo.domain.UserProfile
import com.demo.craftdemo.model.Address
import com.demo.craftdemo.model.CreateProfileRequest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.util.UUID

class DomainModelMapperTest : DescribeSpec ({

    describe("convertToUserProfileEntity"){

        it("should convert to user profile entity"){
            val userId = UUID.randomUUID()
            val addressRequest = Address(
                    lineOne = "testLineOne",
                    lineTwo = "testLineTwo",
                    city = "testCity",
                    state = "testState",
                    zip = "12345",
                    country = "testCountry"
            )
            val createProfileRequest = CreateProfileRequest(
                    companyName = "testCompanyName",
                    legalName = "testLegalName",
                    taxIdentifier = "ASGT",
                    website = "http://localhost",
                    email = "test@gmail.com",
                    companyAddress = addressRequest,
                    legalAddress = addressRequest,
                    userId = userId
            )

            val userProfileEntity = DomainModelMapper.convertToUserProfileEntity(createProfileRequest)

            userProfileEntity.userId.shouldBe(userId)
            userProfileEntity.getCompanyName().shouldBe("testCompanyName")
            userProfileEntity.getLegalName().shouldBe("testLegalName")
            userProfileEntity.getEmail().shouldBe("test@gmail.com")
            userProfileEntity.getWebsite().shouldBe("http://localhost")
            userProfileEntity.getLegalAddress()?.city.shouldBe("testCity")
            userProfileEntity.getLegalAddress()?.state.shouldBe("testState")
            userProfileEntity.getLegalAddress()?.lineOne.shouldBe("testLineOne")
            userProfileEntity.getLegalAddress()?.lineTwo.shouldBe("testLineTwo")
            userProfileEntity.getLegalAddress()?.zip.shouldBe("12345")
            userProfileEntity.getLegalAddress()?.country.shouldBe("testCountry")
        }

    }

    describe("convertToProfileResponse"){

        val address = com.demo.craftdemo.domain.Address(
                lineOne = "testLineOne",
                lineTwo = "testLineTwo",
                city = "testCity",
                state = "testState",
                zip = "12345",
                country = "testCountry"
        )

        val userprofile = UserProfile(
                userId = UUID.randomUUID(),
                companyName = "testCompanyName",
                legalName = "testLegalName",
                taxIdentifier = "ASGT",
                website = "http://localhost",
                email = "test@gmail.com",
                businessAddress = address,
        )

        it("should convert to userprofile response"){

            val userProfileResponse = DomainModelMapper.convertToProfileResponse(userprofile)

            userProfileResponse.companyName.shouldBe("testCompanyName")
            userProfileResponse.legalName.shouldBe("testLegalName")
            userProfileResponse.email.shouldBe("test@gmail.com")
            userProfileResponse.website.shouldBe("http://localhost")
            userProfileResponse.legalAddress?.shouldBeNull()

            userProfileResponse.companyAddress?.city.shouldBe("testCity")
            userProfileResponse.companyAddress?.state.shouldBe("testState")
            userProfileResponse.companyAddress?.lineOne.shouldBe("testLineOne")
            userProfileResponse.companyAddress?.lineTwo.shouldBe("testLineTwo")
            userProfileResponse.companyAddress?.zip.shouldBe("12345")
            userProfileResponse.companyAddress?.country.shouldBe("testCountry")
        }

    }

    describe("updateUserProfileEntity"){

        val userId = UUID.randomUUID()

        val address = com.demo.craftdemo.domain.Address(
                lineOne = "testLineOne",
                lineTwo = "testLineTwo",
                city = "testCity",
                state = "testState",
                zip = "12345",
                country = "testCountry"
        )

        val userprofile = UserProfile(
                userId = userId,
                companyName = "testCompanyName",
                legalName = "testLegalName",
                taxIdentifier = "ASGT",
                website = "http://localhost",
                email = "test@gmail.com",
                businessAddress = address,
        )

        it("should convert to user profile entity"){
            val addressRequest = Address(
                    lineOne = "updatedLineOne",
                    lineTwo = "updatedLineTwo",
                    city = "updatedCity",
                    state = "updatedState",
                    zip = "12345",
                    country = "updatedCountry"
            )
            val createProfileRequest = CreateProfileRequest(
                    companyName = "updatedCompanyName",
                    legalName = "testLegalName",
                    taxIdentifier = "ASGT",
                    website = "http://localhost",
                    email = "test@gmail.com",
                    companyAddress = addressRequest,
                    legalAddress = addressRequest,
                    userId = userId
            )

            val userProfileEntity = DomainModelMapper.updateUserProfileEntity(createProfileRequest, userprofile)

            userProfileEntity.userId.shouldBe(userId)
            userProfileEntity.getCompanyName().shouldBe("updatedCompanyName")
            userProfileEntity.getLegalName().shouldBe("testLegalName")
            userProfileEntity.getEmail().shouldBe("test@gmail.com")
            userProfileEntity.getWebsite().shouldBe("http://localhost")
            userProfileEntity.getLegalAddress()?.city.shouldBe("updatedCity")
            userProfileEntity.getLegalAddress()?.state.shouldBe("updatedState")
            userProfileEntity.getLegalAddress()?.lineOne.shouldBe("updatedLineOne")
            userProfileEntity.getLegalAddress()?.lineTwo.shouldBe("updatedLineTwo")
            userProfileEntity.getLegalAddress()?.zip.shouldBe("12345")
            userProfileEntity.getLegalAddress()?.country.shouldBe("updatedCountry")

            userProfileEntity.getBusinessAddress()?.city.shouldBe("updatedCity")
            userProfileEntity.getBusinessAddress()?.state.shouldBe("updatedState")
            userProfileEntity.getBusinessAddress()?.lineOne.shouldBe("updatedLineOne")
            userProfileEntity.getBusinessAddress()?.lineTwo.shouldBe("updatedLineTwo")
            userProfileEntity.getBusinessAddress()?.zip.shouldBe("12345")
            userProfileEntity.getBusinessAddress()?.country.shouldBe("updatedCountry")
        }

    }
})