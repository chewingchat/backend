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
        testDataGenerator.userSelectedStatusData(userId)
        val result = userStatusRepositoryImpl.readSelectedUserStatus(userId)
        assert(result.isSelected)
    }

    @Test
    fun `선택된 상태 변경 - 아무 것도 없음 처리`() {
        val userId = "userId4"
        testDataGenerator.userStatusDataList(userId)
        testDataGenerator.userSelectedStatusData(userId)
        userStatusRepositoryImpl.updateSelectedStatusFalse(userId)
        val result = userStatusRepositoryImpl.readSelectedUserStatus(userId)
        assert(result.statusId == "none")
    }

    @Test
    fun `선택된 상태 변경 - 선택된 상태가 있음 처리`() {
        val userId = "userId5"
        val userStatus = testDataGenerator.userSelectedStatusData(userId)
        val newUserStatus = testDataGenerator.userStatusData(userId)
        userStatusRepositoryImpl.updateSelectedStatusTrue(userId, newUserStatus.statusId)
        val result = userStatusRepositoryImpl.readSelectedUserStatus(userId)
        assert(result.statusId == newUserStatus.statusId)
    }
}