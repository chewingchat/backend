package org.chewing.v1.repository

import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.repository.support.EmailProvider
import org.chewing.v1.repository.support.MediaProvider
import org.chewing.v1.repository.support.PhoneProvider
import org.chewing.v1.repository.support.UserProvider
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

class UserRepositoryTest2 {
    private val userJpaRepository: UserJpaRepository = mock()

    private var userRepositoryImpl: UserRepositoryImpl = UserRepositoryImpl(userJpaRepository)

    @Test
    fun `유저 아이디로 읽기 - 실패(유저를 찾을 수 없음)`() {
        val userId = "userId"
        whenever(userJpaRepository.findById(userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.read(userId)

        assert(result == null)
    }

    @Test
    fun `이메일로 유저 읽기 - 실페(유저를 찾을 수 없음)`() {
        val contact = EmailProvider.buildNormal()
        whenever(userJpaRepository.findByEmailId(contact.emailId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.readByContact(contact)
        assert(result == null)
    }

    @Test
    fun `휴대폰으로 유저 읽기 - 실패(유저를 찾을 수 없음)`() {
        val contact = PhoneProvider.buildNormal()
        whenever(userJpaRepository.findByPhoneNumberId(contact.phoneId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.readByContact(contact)
        assert(result == null)
    }

    @Test
    fun `유저 삭제 - 실패(유저를 찾을 수 없음)`() {
        val userId = "userId"
        whenever(userJpaRepository.findById(userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.remove(userId)
        assert(result == null)
    }

    @Test
    fun `유저 이미지 변환 - 실패(유저를 찾을 수 없음)`() {
        val media = MediaProvider.buildProfileContent()
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        whenever(userJpaRepository.findById(user.userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.updateMedia(user.userId, media)
        assert(result == null)
    }

    @Test
    fun `유저 배경사진 변환 실패(유저를 찾을 수 없음)`() {
        val media = MediaProvider.buildBackgroundContent()
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        whenever(userJpaRepository.findById(user.userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.updateMedia(user.userId, media)
        assert(result == null)
    }

    @Test
    fun `유저 TTS 변환 실패(유저를 찾을 수 없음)`(){
        val media = MediaProvider.buildTTSContent()
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        whenever(userJpaRepository.findById(user.userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.updateMedia(user.userId, media)
        assert(result == null)
    }


    @Test
    fun `유저 이름 변환 실패(유저를 찾을 수 없음)`(){
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        val userName = UserProvider.buildNewUserName()
        whenever(userJpaRepository.findById(user.userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.updateName(userId, userName)
        assert(result == null)
    }

    @Test
    fun `유저 연락처 변환 실패(유저를 찾을 수 없음)`(){
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        val contact = EmailProvider.buildNormal()
        whenever(userJpaRepository.findById(user.userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.updateContact(userId, contact)
        assert(result == null)
    }

    @Test
    fun `유저 접근권한 변환 실패(유저를 찾을 수 없음)`(){
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        val userContent = UserProvider.buildNewUserContent()
        whenever(userJpaRepository.findById(user.userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.updateAccess(userId, userContent)
        assert(result == null)
    }

    @Test
    fun `유저 생일 변환 실패(유저를 찾을 수 없음)`() {
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        val birth = "birth"
        whenever(userJpaRepository.findById(user.userId)).thenReturn(Optional.empty())

        val result = userRepositoryImpl.updateBirth(userId, birth)
        assert(result == null)
    }
}

