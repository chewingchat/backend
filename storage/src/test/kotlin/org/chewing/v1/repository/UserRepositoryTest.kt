package org.chewing.v1.repository

import org.chewing.v1.config.DbContextTest
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.model.AccessStatus
import org.chewing.v1.repository.support.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest : DbContextTest() {
    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

    private val userRepositoryImpl: UserRepositoryImpl by lazy {
        UserRepositoryImpl(userJpaRepository)
    }

    @Test
    fun `유저 아이디로 읽기`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)

        val result = userRepositoryImpl.readyId(user.userId)

        assert(result!!.userId == user.userId)
    }

    @Test
    fun `유저 이메일로 읽기`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)

        val result = userRepositoryImpl.readByContact(email)

        assert(result!!.userId == user.userId)
    }

    @Test
    fun `이메일로 유저 신규 생성`() {
        val email = EmailProvider.buildNormal()

        val user = userRepositoryImpl.append(email)

        assert(user.userId.isNotEmpty())
    }

    @Test
    fun `휴대폰 으로 유저 신규 생성`() {
        val phone = PhoneProvider.buildNormal()

        val user = userRepositoryImpl.append(phone)

        assert(user.userId.isNotEmpty())
    }

    @Test
    fun `이미 유저가 있다면 신규 생성하면 안됨`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)

        val result = userRepositoryImpl.append(email)

        assert(result.userId == user.userId)
    }

    @Test
    fun `유저 삭제`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)

        userRepositoryImpl.remove(user.userId)

        assert(userJpaRepository.findById(user.userId).get().toUser().status == AccessStatus.DELETE)
    }

    @Test
    fun `유저 이미지 업데이트 - 기본 사진에서 새로운 사진으로 바뀜`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)
        val media = MediaProvider.buildProfileContent()

        userRepositoryImpl.updateImage(user, media)
        val result = userJpaRepository.findById(user.userId).get().toUser()

        assert(result.image.type != media.type)
    }

    @Test
    fun `유저 이미지 업데이트 - 기본 배경 사진에서 새로운 사진으로 바뀜`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)
        val media = MediaProvider.buildBackgroundContent()

        userRepositoryImpl.updateBackgroundImage(user, media)
        val result = userJpaRepository.findById(user.userId).get().toUser()

        assert(result.backgroundImage.type != media.type)
    }

    @Test
    fun `유저 이름 변경`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)
        val newName = UserProvider.buildNewUserName()

        userRepositoryImpl.updateName(user.userId, newName)
        val result = userJpaRepository.findById(user.userId).get().toUser()

        assert(result.name.firstName == newName.firstName)
        assert(user.name.lastName != newName.lastName)
    }


    @Test
    fun `유저 연락처 변경`() {
        val oldEmail = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(oldEmail)
        val newEmail = EmailProvider.buildNew()

        userRepositoryImpl.updateContact(user.userId, newEmail)
        val result = userJpaRepository.findById(user.userId).get().getEmailId()

        assert(result != oldEmail.emailId)
    }

    @Test
    fun `유저 접근 변경`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)
        val newContent = UserProvider.buildNewUserContent()

        userRepositoryImpl.updateAccess(user.userId, newContent)
        val result = userJpaRepository.findById(user.userId).get().toUser()

        assert(result.status == AccessStatus.ACCESS)
        assert(result.name.firstName != user.name.firstName)
        assert(result.birth != user.birth)
        assert(result.name.lastName != user.name.lastName)
    }

    @Test
    fun `연락처가 다른 유저 소유 있는지 확인`() {
        val phone = PhoneProvider.buildNormal()
        val user = testDataGenerator.userEntityPhoneData(phone)
        val newUserId = "newUserId"

        val result = userRepositoryImpl.checkContactIsUsedByElse(phone, newUserId)

        assert(result)
    }

    @Test
    fun `이메일이 다른 유저 소유 인지 확인`(){
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)

        val newUserId  = "newUserId"

        val result = userRepositoryImpl.checkContactIsUsedByElse(email, newUserId)

        assert(result)
    }

    @Test
    fun `유저 생일 변경`() {
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)
        val newBirth = UserProvider.buildNewBirth()

        userRepositoryImpl.updateBirth(user.userId, newBirth)
        val result = userJpaRepository.findById(user.userId).get().toUser()

        assert(result.birth == newBirth)
        assert(result.birth != user.birth)
    }

    @Test
    fun `TTS 파일 경로 수정`(){
        val email = EmailProvider.buildNormal()
        val user = testDataGenerator.userEntityEmailData(email)

        val tts = MediaProvider.buildTTSContent()

        userRepositoryImpl.updateTTS(user.userId, tts)
        val result = userJpaRepository.findById(user.userId).get().toTTS()

        assert(result.url == tts.url)
    }

}