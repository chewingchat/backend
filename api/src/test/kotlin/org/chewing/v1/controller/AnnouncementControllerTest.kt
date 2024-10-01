package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.TestDataFactory.createAnnouncement
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.service.AnnouncementService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.format.DateTimeFormatter

@WebMvcTest(AnnouncementController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class AnnouncementControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var announcementService: AnnouncementService

    @Test
    @DisplayName("공지사항 목록 조회")
    fun getAnnouncements() {
        val announcement = createAnnouncement()
        whenever(announcementService.readAnnouncements()).thenReturn(listOf(announcement))
        val uploadTime = announcement.uploadAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/announcement/list")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "userId")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.announcements[0].announcementId").value(announcement.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.announcements[0].topic").value(announcement.topic))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.announcements[0].uploadTime").value(uploadTime))
    }

    @Test
    @DisplayName("공지사항 조회")
    fun getAnnouncement() {
        val announcement = createAnnouncement()
        whenever(announcementService.readAnnouncement(announcement.id)).thenReturn(announcement)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/announcement/${announcement.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", "userId")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(announcement.content))
    }
}