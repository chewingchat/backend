package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.RestDocsTest
import org.chewing.v1.TestDataFactory
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.controller.chat.ChatRoomController
import org.chewing.v1.facade.ChatRoomFacade
import org.chewing.v1.model.chat.room.ChatRoomSortCriteria
import org.chewing.v1.service.chat.RoomService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.format.DateTimeFormatter

@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class ChatRoomControllerTest : RestDocsTest() {

    private lateinit var chatRoomFacade: ChatRoomFacade
    private lateinit var roomService: RoomService
    private lateinit var chatRoomController: ChatRoomController
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        chatRoomFacade = mock()
        roomService = mock()
        chatRoomController = ChatRoomController(chatRoomFacade, roomService)
        mockMvc = mockController(chatRoomController)
        objectMapper = objectMapper()
    }

    private fun performCommonSuccessResponse(result: ResultActions) {
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.message").value("성공"))
    }

    @Test
    fun `채팅방 리스트 가져오기`() {
        val userId = "userId"
        val sort = "DATE"
        val chatRoom = TestDataFactory.createChatRoom()
        val formatLatestMessageTime =
            chatRoom.latestMessageTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))

        whenever(chatRoomFacade.getChatRooms(userId, ChatRoomSortCriteria.DATE)).thenReturn(listOf(chatRoom))

        mockMvc.perform(
            post("/api/chatRoom/list")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .param("sort", sort),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.chatRooms[0].chatRoomId").value(chatRoom.chatRoomId))
            .andExpect(jsonPath("$.data.chatRooms[0].favorite").value(chatRoom.favorite))
            .andExpect(jsonPath("$.data.chatRooms[0].groupChatRoom").value(chatRoom.groupChatRoom))
            .andExpect(jsonPath("$.data.chatRooms[0].latestMessage").value(chatRoom.latestMessage))
            .andExpect(jsonPath("$.data.chatRooms[0].latestMessageTime").value(formatLatestMessageTime))
            .andExpect(jsonPath("$.data.chatRooms[0].totalUnReadMessage").value(chatRoom.totalUnReadMessage))
            .andExpect(jsonPath("$.data.chatRooms[0].latestPage").value(chatRoom.latestPage))
            .andExpect(jsonPath("$.data.chatRooms[0].latestSeqNumber").value(chatRoom.latestSeqNumber))
            .andExpect(jsonPath("$.data.chatRooms[0].members[0].memberId").value(chatRoom.chatRoomMemberInfos[0].memberId))
            .andExpect(jsonPath("$.data.chatRooms[0].members[0].readSeqNumber").value(chatRoom.chatRoomMemberInfos[0].readSeqNumber))
            .andExpect(jsonPath("$.data.chatRooms[0].members[0].owned").value(chatRoom.chatRoomMemberInfos[0].isOwned))
    }

    @Test
    fun `채팅방 삭제`() {
        val chatRoomIds = listOf("chatRoomId")
        val userId = "userId"

        val result = mockMvc.perform(
            post("/api/chatRoom/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(mapOf("chatRoomIds" to chatRoomIds))),
        )
        performCommonSuccessResponse(result)
    }

    @Test
    fun `그룹 채팅방 삭제`() {
        val chatRoomIds = listOf("chatRoomId")
        val userId = "userId"

        val result = mockMvc.perform(
            post("/api/chatRoom/delete/group")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(mapOf("chatRoomIds" to chatRoomIds))),
        )
        performCommonSuccessResponse(result)
    }

    @Test
    fun `채팅방 생성`() {
        val userId = "userId"
        val friendId = "friendId"

        whenever(roomService.createChatRoom(userId, friendId)).thenReturn("chatRoomId")

        mockMvc.perform(
            post("/api/chatRoom/create")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(mapOf("friendId" to friendId))),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.data.chatRoomId").value("chatRoomId"))
    }

    @Test
    fun `그룹 채팅방 생성`() {
        val userId = "userId"
        val friendIds = listOf("friendId")

        whenever(chatRoomFacade.createGroupChatRoom(userId, friendIds)).thenReturn("chatRoomId")

        mockMvc.perform(
            post("/api/chatRoom/create/group")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(mapOf("friendIds" to friendIds))),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.data.chatRoomId").value("chatRoomId"))
    }

    @Test
    fun `채팅방 초대`() {
        val userId = "userId"
        val chatRoomId = "chatRoomId"
        val friendId = "friendId"

        val result = mockMvc.perform(
            post("/api/chatRoom/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(
                    objectMapper.writeValueAsString(mapOf("chatRoomId" to chatRoomId, "friendId" to friendId)),
                ),
        )
        performCommonSuccessResponse(result)
    }

    @Test
    fun `즐겨찾기 변경`() {
        val userId = "userId"
        val chatRoomId = "chatRoomId"
        val favorite = true

        val result = mockMvc.perform(
            post("/api/chatRoom/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(
                    objectMapper.writeValueAsString(mapOf("chatRoomId" to chatRoomId, "favorite" to favorite)),
                ),
        )
        performCommonSuccessResponse(result)
    }
}
