//package org.chewing.v1.implementation.auth
//
//import org.chewing.v1.implementation.TestDataFactory
//import org.chewing.v1.implementation.user.UserAppender
//import org.chewing.v1.repository.AuthRepository
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertDoesNotThrow
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.whenever
//
//class AuthProcessorTest {
//    private val authRepository: AuthRepository = mock()
//    private val userAppender: UserAppender = mock()
//    private val jwtTokenProvider: JwtTokenProvider = JwtTokenProvider(
//        "mysecretkey12345asdfvasdfvhjaaaaaaaaaaaaaaaaaaaaaaaaaslfdjasdlkr231243123412",
//        1000L * 60 * 15,
//        1000L * 60 * 60 * 24 * 30,
//    )
//    private val authAppender: AuthAppender = AuthAppender(authRepository)
//    private val authRemover: AuthRemover = AuthRemover(authRepository)
//
//    private val authProcessor = AuthProcessor(
//        userAppender,
//        jwtTokenProvider,
//        authAppender,
//        authRemover,
//    )
//
//    @Test
//    fun processLoginTest() {
//        // Given
//        val contact = TestDataFactory.createValidPhoneContact()
//        val user = TestDataFactory.createUser()
//        val token = jwtTokenProvider.createJwtToken(user.userId)
//        whenever(userAppender.appendIfNotExist(contact)).thenReturn(user)
//        // When
//        val (newToken, newUser) = authProcessor.processLogin(contact)
//        // Then
//        // No exception should be thrown
//        assertDoesNotThrow {
//            // 사용자 ID가 일치하는지 검증
//            assertEquals(jwtTokenProvider.getUserIdFromToken(token.accessToken), user.userId)
//            // 토큰이 생성되었는지 검증
//            assertEquals(newToken.accessToken.isNotEmpty(), true)
//            assertEquals(newToken.refreshToken.token.isNotEmpty(), true)
//        }
//    }
//
//    @Test
//    fun processLogoutTest() {
//        // Given
//        val accessToken = jwtTokenProvider.createAccessToken("testUserId")
//        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)
//        // No exception should be thrown
//        assertDoesNotThrow {
//            authProcessor.processLogout(accessToken)
//            assertEquals(userId, "testUserId")
//        }
//    }
//}