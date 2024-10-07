package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.*
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.repository.PushNotificationRepository
import org.chewing.v1.repository.UserRepository
import org.chewing.v1.repository.UserSearchRepository
import org.chewing.v1.repository.UserStatusRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserServiceTest {
    private val userRepository: UserRepository = mock()
    private val pushNotificationRepository: PushNotificationRepository = mock()
    private val userSearchRepository: UserSearchRepository = mock()
    private val fileProcessor: FileProcessor = mock()

    private val userReader =
        UserReader(userRepository, pushNotificationRepository, userSearchRepository)
    private val userUpdater = UserUpdater(userRepository)
    private val userRemover = UserRemover(userRepository, pushNotificationRepository)
    private val userAppender =
        UserAppender(userRepository, pushNotificationRepository, userSearchRepository)
    private val userValidator = UserValidator()

    private val userService =
        UserService(userReader, fileProcessor, userUpdater, userValidator, userRemover, userAppender)


    @Test
    fun `유저 계정 정보를 가져와야함`() {
        val userId = "userId"
        val userAccount = TestDataFactory.createUserAccount()
        whenever(userRepository.readAccount(userId)).thenReturn(userAccount)


        val result = assertDoesNotThrow {
            userService.getUserAccount(userId)
        }

        assert(result == userAccount)
    }

    @Test
    fun `유저 계정 정보가 없어야함`() {
        val userId = "userId"
        whenever(userRepository.readAccount(userId)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.getUserAccount(userId)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `유저를 이메일로 생성해야함`() {
        val verificationCode = "1234"
        val contact = TestDataFactory.createEmail(verificationCode)
        val appToken = TestDataFactory.createAppToken()
        val device = TestDataFactory.createDevice()
        val user = TestDataFactory.createUser()

        whenever(userRepository.append(contact)).thenReturn(user)

        val result = assertDoesNotThrow {
            userService.createUser(contact, appToken, device)
        }

        assert(user == result)
    }

    @Test
    fun `유저의 접근 정보를 업데이트 해야함`() {
        val userId = "userId"
        val userContent = TestDataFactory.createUserContent()

        whenever(userRepository.updateAccess(userId, userContent)).thenReturn(userId)

        assertDoesNotThrow {
            userService.makeAccess(userId, userContent)
        }
    }

    @Test
    fun `유저의 접근 정보 업데이트 실패 - 유저가 없음`() {
        val userId = "userId"
        val userContent = TestDataFactory.createUserContent()

        whenever(userRepository.updateAccess(userId, userContent)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.makeAccess(userId, userContent)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `생일을 업데이트 해야함`() {
        val userId = "userId"
        val birth = "1990-01-01"
        whenever(userRepository.updateBirth(userId, birth)).thenReturn(userId)

        assertDoesNotThrow {
            userService.updateBirth(userId, birth)
        }
    }

    @Test
    fun `생일 업데이트 실패 - 유저가 없음`() {
        val userId = "userId"
        val birth = "1990-01-01"
        whenever(userRepository.updateBirth(userId, birth)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.updateBirth(userId, birth)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `유저의 이름을 업데이트 해야함`() {
        val userId = "userId"
        val userName = TestDataFactory.createUserName()
        whenever(userRepository.updateName(userId, userName)).thenReturn(userId)

        assertDoesNotThrow {
            userService.updateName(userId, userName)
        }
    }

    @Test
    fun `유저의 이름 업데이트 실패 - 유저가 없음`() {
        val userId = "userId"
        val userName = TestDataFactory.createUserName()
        whenever(userRepository.updateName(userId, userName)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.updateName(userId, userName)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `유저의 연락처를 업데이트 해야함`() {
        val userId = "userId"
        val verificationCode = "1234"
        val contact = TestDataFactory.createPhone(verificationCode)
        whenever(userRepository.updateContact(userId, contact)).thenReturn(userId)

        assertDoesNotThrow {
            userService.updateUserContact(userId, contact)
        }
    }

    @Test
    fun `유저의 이메일을 업데이트 해야함`() {
        val userId = "userId"
        val verificationCode = "1234"
        val contact = TestDataFactory.createEmail(verificationCode)
        whenever(userRepository.updateContact(userId, contact)).thenReturn(userId)

        assertDoesNotThrow {
            userService.updateUserContact(userId, contact)
        }
    }

    @Test
    fun `유저의 연락처 업데이트 실패 - 유저가 없음`() {
        val userId = "userId"
        val verificationCode = "1234"
        val contact = TestDataFactory.createPhone(verificationCode)
        whenever(userRepository.updateContact(userId, contact)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.updateUserContact(userId, contact)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `유저의 이메일 업데이트 실패 - 유저가 없음`() {
        val userId = "userId"
        val verificationCode = "1234"
        val contact = TestDataFactory.createEmail(verificationCode)
        whenever(userRepository.updateContact(userId, contact)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.updateUserContact(userId, contact)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `유저의 통합된 정보를 가져와야함`() {
        val userId = "userId"
        val user = TestDataFactory.createUser()
        whenever(userRepository.read(userId)).thenReturn(user)

        val result = assertDoesNotThrow {
            userService.getAccessUser(userId)
        }

        assert(result == user)
    }

    @Test
    fun `유저의 통합된 정보를 가져오는데 유저가 없음`() {
        val userId = "userId"
        whenever(userRepository.read(userId)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.getAccessUser(userId)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `유저의 통합던 정보를 가져오는 활성화된 유저가 아님`() {
        val userId = "userId"
        val user = TestDataFactory.createNotAccessUser()
        whenever(userRepository.read(userId)).thenReturn(user)

        val result = assertThrows<ConflictException> {
            userService.getAccessUser(userId)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_ACCESS)
    }

    @Test
    fun `유저의 파일을 카테고리에 따라 없데이트 해야함`() {
        val userId = "userId"
        val fileData = TestDataFactory.createFileData()
        val media = TestDataFactory.createMedia()
        whenever(userRepository.updateMedia(userId, media)).thenReturn(media)
        whenever(fileProcessor.processNewFile(userId, fileData, FileCategory.PROFILE)).thenReturn(media)

        assertDoesNotThrow {
            userService.updateFile(fileData, userId, FileCategory.PROFILE)
        }
    }

    @Test
    fun `유저의 파일을 카테고리에 따라 업데이트 할때 유저가 존재하지 않음`(){
        val userId = "userId"
        val fileData = TestDataFactory.createFileData()
        val media = TestDataFactory.createMedia()
        whenever(userRepository.updateMedia(userId, media)).thenReturn(null)
        whenever(fileProcessor.processNewFile(userId, fileData, FileCategory.PROFILE)).thenReturn(media)

        val result = assertThrows<NotFoundException> {
            userService.updateFile(fileData, userId, FileCategory.PROFILE)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun `유저를 삭제함`(){
        val userId = "userId"
        val user = TestDataFactory.createUser()
        whenever(userRepository.remove(userId)).thenReturn(user)

        assertDoesNotThrow {
            userService.deleteUser(userId)
        }
    }

    @Test
    fun `유저를 삭제할때 유저가 없음`(){
        val userId = "userId"
        whenever(userRepository.remove(userId)).thenReturn(null)

        val result = assertThrows<NotFoundException> {
            userService.deleteUser(userId)
        }

        assert(result.errorCode == ErrorCode.USER_NOT_FOUND)
    }
}