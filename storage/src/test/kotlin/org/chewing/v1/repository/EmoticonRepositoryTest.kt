package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jparepository.emoticon.EmoticonJpaRepository
import org.chewing.v1.jparepository.emoticon.EmoticonPackJpaRepository
import org.chewing.v1.repository.emoticon.EmoticonRepositoryImpl
import org.chewing.v1.repository.support.JpaDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class EmoticonRepositoryTest : JpaContextTest() {
    @Autowired
    private lateinit var emoticonJpaRepository: EmoticonJpaRepository

    @Autowired
    private lateinit var emotionPackJpaRepository: EmoticonPackJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val emotionRepositoryImpl: EmoticonRepositoryImpl by lazy {
        EmoticonRepositoryImpl(emoticonJpaRepository, emotionPackJpaRepository)
    }

    @Test
    fun `이모티콘 팩 조회 해서 변환 성공`() {
        val emoticonPackInfo = jpaDataGenerator.emoticonPackEntityData()
        val result = emotionRepositoryImpl.readEmoticonPacks(listOf(emoticonPackInfo.id))

        assert(result[0].id == emoticonPackInfo.id)
        assert(result[0].name == emoticonPackInfo.name)
        assert(result[0].url == emoticonPackInfo.url)
    }

    @Test
    fun `이모티콘 조회 해서 변환 성공`() {
        val emoticonPackId = "emoticonPackId"
        val emoticonInfo = jpaDataGenerator.emoticonEntityData(emoticonPackId)
        val result = emotionRepositoryImpl.readEmoticons(listOf(emoticonPackId))

        assert(result[0].emoticonPackId == emoticonInfo.emoticonPackId)
        assert(result[0].id == emoticonInfo.id)
        assert(result[0].url == emoticonInfo.url)
    }

    @Test
    fun `이모티콘 리스트 조회`() {
        val emoticonPackId = "emoticonPackId2"
        jpaDataGenerator.emoticonEntityDataList(emoticonPackId)
        val result = emotionRepositoryImpl.readEmoticons(listOf(emoticonPackId))
        assert(result.size == 10)
    }

}