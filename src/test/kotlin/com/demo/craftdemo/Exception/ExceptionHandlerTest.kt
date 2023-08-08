package com.demo.craftdemo.Exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

class ExceptionHandlerTest : DescribeSpec({

    lateinit var exceptionHandler: ExceptionHandler

    beforeTest {
        exceptionHandler = ExceptionHandler()
    }

    describe("onMethodArgumentTypeMismatchException"){

        it("should return response entity with bad request"){
            val exception = MethodArgumentTypeMismatchException(
                "Test",
                Integer::class.java,
                "zip",
                mockk(),
                NumberFormatException()
            )
            val expectedResponse = ErrorResponse(
                    field = "zip",
                    message = "zip is not in valid format"
            )

            val response = exceptionHandler.onMethodArgumentTypeMismatchException(exception)


            response.statusCode.shouldBe(HttpStatus.BAD_REQUEST)
            response.body.shouldBe(expectedResponse)
        }
    }

    describe("onBadRequestException"){

        it("should return response entity with bad request"){
            val exception = BadRequestException("Failed")
            val expectedResponse = ErrorResponse(
                    message = "Failed"
            )

            val response = exceptionHandler.onBadRequestException(exception)


            response.statusCode.shouldBe(HttpStatus.BAD_REQUEST)
            response.body.shouldBe(expectedResponse)
        }
    }


    describe("onIllegalStateException"){

        it("should return response entity with bad request"){
            val exception = IllegalStateException("Failed")
            val expectedResponse = ErrorResponse(
                    message = "Failed"
            )

            val response = exceptionHandler.onIllegalStateException(exception)


            response.statusCode.shouldBe(HttpStatus.BAD_REQUEST)
            response.body.shouldBe(expectedResponse)
        }
    }

    describe("onHttpMessageNotReadableException"){

        it("should return response entity with bad request"){
            val field = "zip"
            val mockMissingKotlinParameterException = mockk<MissingKotlinParameterException>()
            val mockHttpMessageNotReadableException = mockk<HttpMessageNotReadableException>()
            val expectedResponse = ErrorResponse(
                    field = field,
                    message = "Field is missing"
            )


            every { mockHttpMessageNotReadableException.rootCause } returns mockMissingKotlinParameterException
            every { mockMissingKotlinParameterException.parameter.name } returns field

            val response = exceptionHandler.onHttpMessageNotReadableException(mockHttpMessageNotReadableException)


            response.statusCode.shouldBe(HttpStatus.BAD_REQUEST)
            response.body.shouldBe(expectedResponse)
        }

        it("should return response entity with bad request for invalidFormatException"){
            val mockInvalidFormatExceptionException = mockk<InvalidFormatException>()
            val mockHttpMessageNotReadableException = mockk<HttpMessageNotReadableException>()
            val expectedResponse = ErrorResponse(
                    message = "Unable to parse the request payload"
            )

            every { mockHttpMessageNotReadableException.rootCause } returns mockInvalidFormatExceptionException

            val response = exceptionHandler.onHttpMessageNotReadableException(mockHttpMessageNotReadableException)


            response.statusCode.shouldBe(HttpStatus.BAD_REQUEST)
            response.body.shouldBe(expectedResponse)
        }
    }


})
