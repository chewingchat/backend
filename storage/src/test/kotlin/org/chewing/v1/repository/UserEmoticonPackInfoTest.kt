package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jparepository.user.UserEmoticonJpaRepository
import org.chewing.v1.repository.support.JpaDataGenerator
import org.chewing.v1.repository.user.UserEmoticonRepositoryImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserEmoticonPackInfoTest : JpaContextTest() {

    @Autowired
    private lateinit var userEmoticonJpaRepository: UserEmoticonJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val userEmoticonRepositoryImpl: UserEmoticonRepositoryImpl by lazy {
        UserEmoticonRepositoryImpl(userEmoticonJpaRepository)
    }

    @Test
    fun `유저 이모티콘 조회`() {
        val userId = "userId"
        val emoticonPackId = "emoticonPackId"
        val userEmoticon = jpaDataGenerator.userEmoticonEntityData(userId, emoticonPackId)
        val result = userEmoticonRepositoryImpl.readUserEmoticons(userId)

        assert(result[0].userId == userEmoticon.userId)
        assert(result[0].emoticonPackId == userEmoticon.emoticonPackId)
        assert(result[0].createAt == userEmoticon.createAt)
    }
}