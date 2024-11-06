package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jparepository.user.UserStatusJpaRepository
import org.chewing.v1.repository.support.JpaDataGenerator
import org.chewing.v1.repository.user.UserStatusRepositoryImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserStatusRepositoryTest : JpaContextTest() {
    @Autowired
    private lateinit var userStatusJpaRepository: UserStatusJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val userStatusRepositoryImpl: UserStatusRepositoryImpl by lazy {
        UserStatusRepositoryImpl(userStatusJpaRepository)
    }

    @Test
    fun `유저 아이디로 읽기`() {
        val userId = "userId"
        jpaDataGenerator.userStatusDataList(userId)
        val result = userStatusRepositoryImpl.reads(userId)
        assert(result.isNotEmpty())
    }

    @Test
    fun `추가 하기`() {
        val userId = "userId2"
        val userStatus = jpaDataGenerator.userStatusData(userId)
        userStatusRepositoryImpl.append(userStatus.userId, userStatus.message, userStatus.emoji)
        val result = userStatusJpaRepository.findById(userStatus.statusId).get().toUserStatus()
        assert(result.userId == userStatus.userId)
    }

    @Test
    fun `선택된 상태 읽기`() {
        val userId = "userId3"
        jpaDataGenerator.userStatusDataList(userId)
        val status = jpaDataGenerator.userSelectedStatusData(userId)
        val result = userStatusRepositoryImpl.readSelected(userId)
        assert(result.isSelected)
        assert(result.statusId == status.statusId)
        assert(result.statusId != "none")
    }

    @Test
    fun `선택된 상태 변경 - 아무 것도 없음 처리`() {
        val userId = "userId4"
        jpaDataGenerator.userStatusDataList(userId)
        jpaDataGenerator.userSelectedStatusData(userId)
        userStatusRepositoryImpl.updateSelectedStatusFalse(userId)
        val result = userStatusJpaRepository.findBySelectedTrueAndUserId(userId)
        assert(result.isEmpty)
    }

    @Test
    fun `선택된 상태 변경 - 선택된 상태가 있음 처리`() {
        val userId = "userId5"
        val newUserStatus = jpaDataGenerator.userStatusData(userId)
        userStatusRepositoryImpl.updateSelectedStatusTrue(userId, newUserStatus.statusId)
        val result = userStatusJpaRepository.findBySelectedTrueAndUserId(userId).get().toUserStatus()
        assert(result.statusId == newUserStatus.statusId)
    }

    @Test
    fun `유저가 가지고 있는 모든 상태 설정 삭제`() {
        val userId = "userId6"
        jpaDataGenerator.userStatusDataList(userId)
        userStatusRepositoryImpl.removeAllByUserId(userId)
        val result = userStatusJpaRepository.findAllByUserId(userId)
        assert(result.isEmpty())
    }

    @Test
    fun `statusIds로 삭제`() {
        val userId = "userId7"
        val userStatus = jpaDataGenerator.userStatusData(userId)
        val userStatus2 = jpaDataGenerator.userStatusData(userId)
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
        jpaDataGenerator.userStatusDataList(userId)
        jpaDataGenerator.userStatusDataList(userId2)
        jpaDataGenerator.userSelectedStatusData(userId)
        jpaDataGenerator.userSelectedStatusData(userId2)
        val result = userStatusRepositoryImpl.readSelectedUsers(listOf(userId, userId2))
        assert(result.size == 2)
    }
}
