package org.chewing.v1.repository

import org.chewing.v1.config.DbContextTest
import org.chewing.v1.jparepository.UserStatusJpaRepository
import org.chewing.v1.repository.support.TestDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class UserStatusRepositoryTest : DbContextTest() {
    @Autowired
    private lateinit var userStatusJpaRepository: UserStatusJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

    private val userStatusRepositoryImpl: UserStatusRepositoryImpl by lazy {
        UserStatusRepositoryImpl(userStatusJpaRepository)
    }


    @Test
    fun `유저 아이디로 읽기`() {
        val userId = "userId"
        testDataGenerator.userStatusDataList(userId)
        val result = userStatusRepositoryImpl.readsUserStatus(userId)
        assert(result.isNotEmpty())
    }

    @Test
    fun `추가 하기`() {
        val userId = "userId2"
        val userStatus = testDataGenerator.userStatusData(userId)
        userStatusRepositoryImpl.append(userStatus.userId, userStatus.message, userStatus.emoji)
        val result = userStatusJpaRepository.findById(userStatus.statusId).get().toUserStatus()
        assert(result.userId == userStatus.userId)
    }

    @Test
    fun `선택된 상태 읽기`() {
        val userId = "userId3"
        testDataGenerator.userStatusDataList(userId)
        val status = testDataGenerator.userSelectedStatusData(userId)
        val result = userStatusRepositoryImpl.readSelectedUserStatus(userId)
        assert(result.isSelected)
        assert(result.statusId == status.statusId)
        assert(result.statusId != "none")
    }

    @Test
    fun `선택된 상태 변경 - 아무 것도 없음 처리`() {
        val userId = "userId4"
        testDataGenerator.userStatusDataList(userId)
        testDataGenerator.userSelectedStatusData(userId)
        userStatusRepositoryImpl.updateSelectedStatusFalse(userId)
        val result = userStatusJpaRepository.findBySelectedTrueAndUserId(userId)
        assert(result.isEmpty)
    }


    @Test
    fun `선택된 상태 변경 - 선택된 상태가 있음 처리`() {
        val userId = "userId5"
        val newUserStatus = testDataGenerator.userStatusData(userId)
        userStatusRepositoryImpl.updateSelectedStatusTrue(userId, newUserStatus.statusId)
        val result = userStatusJpaRepository.findBySelectedTrueAndUserId(userId).get().toUserStatus()
        assert(result.statusId == newUserStatus.statusId)
    }

    @Test
    fun `유저가 가지고 있는 모든 상태 설정 삭제`() {
        val userId = "userId6"
        testDataGenerator.userStatusDataList(userId)
        userStatusRepositoryImpl.removeAllByUserId(userId)
        val result = userStatusJpaRepository.findAllByUserId(userId)
        assert(result.isEmpty())
    }

    @Test
    fun `statusIds로 삭제`() {
        val userId = "userId7"
        val userStatus = testDataGenerator.userStatusData(userId)
        val userStatus2 = testDataGenerator.userStatusData(userId)
        userStatusRepositoryImpl.removes(listOf(userStatus.statusId, userStatus2.statusId))
        val result = userStatusJpaRepository.findById(userStatus.statusId)
        val result2 = userStatusJpaRepository.findById(userStatus2.statusId)
        assert(result.isEmpty)
        assert(result2.isEmpty)
    }

    @Test
    fun `여러 유저의 선택된 상태 읽기`() {
        val userId = "userId8"
        val userId2 = "userId9"
        testDataGenerator.userStatusDataList(userId)
        testDataGenerator.userStatusDataList(userId2)
        testDataGenerator.userSelectedStatusData(userId)
        testDataGenerator.userSelectedStatusData(userId2)
        val result = userStatusRepositoryImpl.readSelectedUsersStatus(listOf(userId, userId2))
        assert(result.size == 2)
    }
}