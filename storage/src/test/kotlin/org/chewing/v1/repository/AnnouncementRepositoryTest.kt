package org.chewing.v1.repository

import org.chewing.v1.config.DbContextTest
import org.chewing.v1.jparepository.AnnouncementJpaRepository
import org.chewing.v1.repository.support.TestDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AnnouncementRepositoryTest : DbContextTest() {
    @Autowired
    private lateinit var announcementJpaRepository: AnnouncementJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

    private val announcementRepositoryImpl: AnnouncementRepositoryImpl by lazy {
        AnnouncementRepositoryImpl(announcementJpaRepository)
    }

    @Test
    fun `공지사항 조회에 성공해야 한다`() {
        val announcement = testDataGenerator.announcementEntityData()

        val result = announcementRepositoryImpl.read(announcement.id)

        assert(result != null)
        assert(result!!.topic == announcement.topic)
        assert(result.content == announcement.content)
    }

    @Test
    fun `공지사항 조회에 실패해야 한다`() {
        testDataGenerator.announcementEntityData()

        val result = announcementRepositoryImpl.read("wrongId")

        assert(result == null)
    }

    @Test
    fun `공지사항 목록 조회에 성공해야 한다`() {
        testDataGenerator.announcementEntityDataList()

        val result = announcementRepositoryImpl.reads()

        assert(result.isNotEmpty())
        val createdAtList = result.map { it.uploadAt }
        assert(createdAtList == createdAtList.sorted())
    }
}