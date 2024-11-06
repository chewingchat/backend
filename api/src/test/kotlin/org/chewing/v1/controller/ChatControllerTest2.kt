package org.chewing.v1.controller

import org.chewing.v1.RestDocsTest
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.controller.chat.ChatController
import org.chewing.v1.facade.ChatFacade
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class ChatControllerTest2 : RestDocsTest() {
    private lateinit var chatFacade: ChatFacade
    private lateinit var chatController: ChatController

    @BeforeEach
    fun setUp() {
        chatFacade = mock()
        chatController = ChatController(chatFacade)
        mockMvc = mockController(chatController)
    }

    private fun performCommonSuccessCreateResponse(result: ResultActions) {
        result.andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(201))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("생성 완료"))
    }

    @Test
    @DisplayName("채팅방 파일 추가 테스트")
    fun `uploadFiles`() {
        val mockFile1 = MockMultipartFile(
            "files",
            "0.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "Test content".toByteArray(),
        )
        val mockFile2 = MockMultipartFile(
            "files",
            "1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "Test content".toByteArray(),
        )

        // When: 파일 업로드 요청을 보냄
        val result = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/chat/file/upload")
                .file(mockFile1)
                .file(mockFile2)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .requestAttr("userId", "testUserId") // userId 전달
                .param("chatRoomId", "testRoomId"),

        )
        performCommonSuccessCreateResponse(result)
    }
}
