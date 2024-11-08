package org.chewing.v1.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ConflictException
import org.chewing.v1.support.TestExceptionController
import org.chewing.v1.support.TestRequest
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.facade.AccountFacade
import org.chewing.v1.facade.MainFacade
import org.chewing.v1.response.ErrorResponse
import org.chewing.v1.service.auth.AuthService
import org.chewing.v1.support.TestExceptionService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(TestExceptionController::class)
@Import(TestSecurityConfig::class, GlobalExceptionHandler::class) // GlobalExceptionHandler 포함
@ActiveProfiles("test")
class GlobalExceptionHandlerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var testExceptionService: TestExceptionService

    @Test
    fun `MissingServletRequestParameterException 처리 확인`() {
        val requestBody = mapOf(
            "test" to 0
        )
        // 'test' 파라미터를 누락하여 예외 유발
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)) // 'test' 파라미터 누락
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.VARIABLE_WRONG.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.VARIABLE_WRONG).message))
    }

    @Test
    fun `HttpMessageNotReadableException 처리 확인 - 빈 본문`() {
        // 빈 본문 전송하여 예외 유발
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue") // 'test' 파라미터 포함
                .content("") // 빈 본문 전송
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.VARIABLE_WRONG.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.VARIABLE_WRONG).message))
    }

    @Test
    fun `HttpMessageNotReadableException 처리 확인 - 잘못된 JSON`() {
        val requestBody = mapOf(
            "test" to "testValue"
        )
        // 잘못된 JSON 전송하여 예외 유발
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue") // 'test' 파라미터 포함
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.VARIABLE_WRONG.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.VARIABLE_WRONG).message))
    }



    @Test
    fun `AuthorizationException 처리 확인`() {
        val requestBody = mapOf(
            "test" to 0
        )
        whenever(testExceptionService.testException())
            .thenThrow(AuthorizationException(ErrorCode.EXPIRED_VALIDATE_CODE))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue")
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.EXPIRED_VALIDATE_CODE.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.EXPIRED_VALIDATE_CODE).message))
    }

    @Test
    fun `NotFoundException 처리 확인`() {
        val requestBody = mapOf(
            "test" to 0
        )
        whenever(testExceptionService.testException())
            .thenThrow(NotFoundException(ErrorCode.USER_NOT_FOUND))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue")
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.USER_NOT_FOUND.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.USER_NOT_FOUND).message))
    }

    @Test
    fun `ConflictException 처리 확인`() {
        val requestBody = mapOf(
            "test" to 0
        )
        whenever(testExceptionService.testException())
            .thenThrow(ConflictException(ErrorCode.FILE_DELETE_FAILED))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue")
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isConflict)
            .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.FILE_DELETE_FAILED.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.FILE_DELETE_FAILED).message))
    }

    @Test
    fun `HttpRequestMethodNotSupportedException 처리 확인`() {
        val requestBody = mapOf(
            "test" to 0
        )
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue")
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.PATH_WRONG.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.PATH_WRONG).message))
    }

    @Test
    fun `IllegalArgumentException 처리 확인`() {
        val requestBody = mapOf(
            "test" to 0
        )
        whenever(testExceptionService.testException())
            .thenThrow(IllegalArgumentException("잘못된 인자"))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue")
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.VARIABLE_WRONG.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.VARIABLE_WRONG).message))

        verify(testExceptionService).testException()
    }

    @Test
    fun `Generic Exception 처리 확인`() {
        val requestBody = mapOf(
            "test" to 0
        )
        whenever(testExceptionService.testException())
            .thenThrow(RuntimeException("서버 오류"))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .param("test", "testValue")
                .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
            .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.INTERNAL_SERVER_ERROR.code))
            .andExpect(jsonPath("$.data.message").value(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR).message))
    }
}
