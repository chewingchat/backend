package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.TestDataFactory.createSchedule
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.controller.user.UserScheduleController
import org.chewing.v1.service.user.ScheduleService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
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

@WebMvcTest(UserScheduleController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class UserScheduleControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var scheduleService: ScheduleService

    @Test
    fun getSchedule() {
        val schedules = listOf(createSchedule())
        val year = 2021
        val month = 1
        val formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")
        val startTime = schedules[0].time.startAt.format(formatter)
        val endTime = schedules[0].time.endAt.format(formatter)
        val notificationTime = schedules[0].time.notificationAt.format(formatter)

        // When
        whenever(scheduleService.fetches(any(), any())).thenReturn(schedules)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", year.toString())
                .param("month", month.toString())
                .requestAttr("userId", "testUserId")  // userId 전달
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.schedules[0].scheduleId").value(schedules[0].id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.schedules[0].title").value(schedules[0].content.title))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.schedules[0].memo").value(schedules[0].content.memo))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.schedules[0].startTime").value(startTime)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.schedules[0].endTime").value(endTime)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data.schedules[0].notificationTime")
                    .value(notificationTime)
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.schedules[0].location").value(schedules[0].content.location))
    }

    @Test
    fun deleteSchedule() {
        val scheduleId = "testScheduleId"

        // When
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapOf("scheduleId" to scheduleId)))
                .requestAttr("userId", "testUserId")  // userId 전달
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("생성 완료"))
    }

    @Test
    fun addSchedule() {

        val requestBody = mapOf(
            "title" to "testTitle",
            "memo" to "testMemo",
            "startTime" to "2021-01-01 00:00:00",
            "endTime" to "2021-01-01 00:00:00",
            "notificationTime" to "2021-01-01 00:00:00",
            "location" to "testLocation"
        )

        // When
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .requestAttr("userId", "testUserId")  // userId 전달
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("성공"))
    }
}