package org.chewing.v1.facade

import org.chewing.v1.TestDataFactory
import org.chewing.v1.model.contact.ContactType
import org.chewing.v1.service.AuthService
import org.chewing.v1.service.ScheduleService
import org.chewing.v1.service.UserService
import org.chewing.v1.service.UserStatusService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AccountFacadeTest {
    private val authService: AuthService = mock()
    private val userService: UserService = mock()
    private val userStatusService: UserStatusService = mock()
    private val scheduleService: ScheduleService = mock()

    private val accountFacade = AccountFacade(authService, userService, userStatusService, scheduleService)

    @Test
    fun `로그인 및 유저 생성`() {
        val userId = "123"
        val email = TestDataFactory.createEmail("123")
        val user = TestDataFactory.createUser(userId)
        val emailAddress = TestDataFactory.createEmailAddress()
        val loginInfo = TestDataFactory.createLoginInfo(user)
        val device = TestDataFactory.createDevice()

        whenever(authService.verify(any(), any())).thenReturn(email)
        whenever(userService.createUser(any(), any(), any())).thenReturn(user)
        whenever(authService.createLoginInfo(any())).thenReturn(loginInfo)

        val result = assertDoesNotThrow {
            accountFacade.loginAndCreateUser(emailAddress, "123", "testAppToken", device)
        }
        assert(result == loginInfo)
    }

    @Test
    fun `인증 정보 변경`() {
        val userId = "123"
        val email = TestDataFactory.createEmail("123")
        val emailAddress = TestDataFactory.createEmailAddress()

        whenever(authService.verify(any(), any())).thenReturn(email)

        assertDoesNotThrow {
            accountFacade.changeCredential(userId, emailAddress, "123")
        }

    }

    @Test
    fun `계정 삭제`() {
        val userId = "123"

        assertDoesNotThrow {
            accountFacade.deleteAccount(userId)
        }
    }

    @Test
    fun `계정 조회 - 휴대폰은 none이 들어가야 함`() {
        val userId = "123"
        val emailId = "123"
        val userAccount = TestDataFactory.createUserAccount(emailId, null)
        val email = TestDataFactory.createEmail("123")

        whenever(userService.getUserAccount(any())).thenReturn(userAccount)
        whenever(authService.getContactById(userAccount.emailId!!, ContactType.EMAIL)).thenReturn(email)

        val result = assertDoesNotThrow {
            accountFacade.getAccount(userId)
        }
        assert(result.user == userAccount.user)
        assert(result.emailAddress == email.emailAddress)
        assert(result.phoneNumber == "none")
        assert(result.countryCode == "none")
    }

    @Test
    fun `계정 조회 - 이메일은 none이 들어가야 함`() {
        val userId = "123"
        val phoneId = "123"
        val userAccount = TestDataFactory.createUserAccount(null, phoneId)
        val phone = TestDataFactory.createPhone("123")

        whenever(userService.getUserAccount(any())).thenReturn(userAccount)
        whenever(authService.getContactById(userAccount.phoneId!!, ContactType.PHONE)).thenReturn(phone)

        val result = assertDoesNotThrow {
            accountFacade.getAccount(userId)
        }
        assert(result.user == userAccount.user)
        assert(result.emailAddress == "none")
        assert(result.phoneNumber == phone.number)
        assert(result.countryCode == phone.countryCode)
    }
}