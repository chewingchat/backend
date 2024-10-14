package org.chewing.v1.controller

import org.chewing.v1.dto.request.chat.ChatRoomDeleteRequest
import org.chewing.v1.dto.request.chat.ChatRoomRequest
import org.chewing.v1.dto.response.chat.ChatRoomResponse
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.ChatRoomService
import org.chewing.v1.util.ResponseHelper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chatRooms")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {

    // 채팅방 목록 가져오기
    @PostMapping("/list")
    fun getChatRooms(
        @RequestBody request: ChatRoomRequest
    ): ResponseEntity<HttpResponse<List<ChatRoomResponse>>> {
        val chatRooms = chatRoomService.getChatRooms(request.sort)
        return ResponseHelper.success(chatRooms.map { ChatRoomResponse.from(it) })
    }

    // 채팅방 삭제
    @PostMapping("/delete")
    fun deleteChatRooms(
        @RequestBody request: ChatRoomDeleteRequest,
        @RequestAttribute("userId") userId: String
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        chatRoomService.leaveChatRooms(request.chatRoomIds, userId)
        return ResponseHelper.successOnly()
    }
}