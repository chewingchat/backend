package org.chewing.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.chewing.v1.TestDataFactory
import org.chewing.v1.config.TestSecurityConfig
import org.chewing.v1.controller.search.SearchController
import org.chewing.v1.facade.SearchFacade
import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.service.search.SearchService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebMvcTest(SearchController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class SearchControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var searchFacade: SearchFacade

    @MockBean
    private lateinit var searchService: SearchService

    private fun performCommonSuccessCreateResponse(result: ResultActions) {
        result.andExpect(status().isCreated)
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.data.message").value("생성 완료"))
    }


    @Test
    fun `검색 키워드 추가`() {
        val userId = "userId"
        val keyword = "keyword"
        val requestBody = mapOf(
            "keyword" to keyword
        )
        val result = mockMvc.perform(
            post("/api/search")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(requestBody))
        )
        performCommonSuccessCreateResponse(result)
    }

    @Test
    fun `최근 검색 키워드 조회`() {
        val userId = "userId"
        val time = LocalDateTime.now()
        val userSearch1 = UserSearch.of("keyword1", time)
        val userSearch2 = UserSearch.of("keyword2", time)
        whenever(searchService.getSearchKeywords(userId)).thenReturn(listOf(userSearch1, userSearch2))
        val result = mockMvc.perform(
            get("/api/search/recent")
                .requestAttr("userId", userId)
        )
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.keywords[0].keyword").value("keyword1"))
            .andExpect(jsonPath("$.data.keywords[1].keyword").value("keyword2"))
    }

    @Test
    fun `키워드로 검색`(){
        val userId = "userId"
        val keyword = "keyword"
        val chatRoom = TestDataFactory.createChatRoom()
        val friendShip = TestDataFactory.createFriendShip()
        val search = TestDataFactory.createSearch(listOf(chatRoom), listOf(friendShip))
        val latestMessageTime = chatRoom.latestMessageTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))

        whenever(searchFacade.search(userId, keyword)).thenReturn(search)

        val result = mockMvc.perform(
            get("/api/search")
                .requestAttr("userId", userId)
                .param("keyword", keyword)
        )

        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.chatRooms[0].chatRoomId").value(chatRoom.chatRoomId))
            .andExpect(jsonPath("$.data.chatRooms[0].favorite").value(chatRoom.favorite))
            .andExpect(jsonPath("$.data.chatRooms[0].groupChatRoom").value(chatRoom.groupChatRoom))
            .andExpect(jsonPath("$.data.chatRooms[0].latestMessage").value(chatRoom.latestMessage))
            .andExpect(jsonPath("$.data.chatRooms[0].latestMessageTime").value(latestMessageTime))
            .andExpect(jsonPath("$.data.chatRooms[0].totalUnReadMessage").value(chatRoom.totalUnReadMessage))
            .andExpect(jsonPath("$.data.chatRooms[0].latestPage").value(chatRoom.latestPage))
            .andExpect(jsonPath("$.data.chatRooms[0].latestSeqNumber").value(chatRoom.latestSeqNumber))
            .andExpect(jsonPath("$.data.chatRooms[0].members[0].memberId").value(chatRoom.chatRoomMemberInfos[0].memberId))
            .andExpect(jsonPath("$.data.chatRooms[0].members[0].owned").value(chatRoom.chatRoomMemberInfos[0].isOwned))
            .andExpect(jsonPath("$.data.chatRooms[0].members[0].readSeqNumber").value(chatRoom.chatRoomMemberInfos[0].readSeqNumber))
            .andExpect(jsonPath("$.data.chatRooms[0].members[1].memberId").value(chatRoom.chatRoomMemberInfos[1].memberId))
            .andExpect(jsonPath("$.data.chatRooms[0].members[1].owned").value(chatRoom.chatRoomMemberInfos[1].isOwned))
            .andExpect(jsonPath("$.data.chatRooms[0].members[1].readSeqNumber").value(chatRoom.chatRoomMemberInfos[1].readSeqNumber))

        result.andExpect(jsonPath("$.data.friends[0].friendId").value(friendShip.friendId))
            .andExpect(jsonPath("$.data.friends[0].firstName").value(friendShip.friendName.firstName))
            .andExpect(jsonPath("$.data.friends[0].lastName").value(friendShip.friendName.lastName))
            .andExpect(jsonPath("$.data.friends[0].favorite").value(friendShip.isFavorite))


    }

}