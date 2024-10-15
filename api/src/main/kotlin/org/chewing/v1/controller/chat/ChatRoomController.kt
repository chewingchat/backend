package org.chewing.v1.controller.chat

import org.chewing.v1.dto.request.chat.ChatRoomRequest
import org.chewing.v1.dto.response.chat.ChatRoomIdResponse
import org.chewing.v1.dto.response.chat.ChatRoomResponse
import org.chewing.v1.facade.ChatRoomFacade
import org.chewing.v1.model.chat.room.ChatRoomSortCriteria
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.chat.RoomService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chatRooms")
class ChatRoomController(
    private val chatRoomFacade: ChatRoomFacade,
    private val roomService: RoomService
) {

    // 채팅방 목록 가져오기
    // sort 추가 해야함
    @PostMapping("/list")
    fun getChatRooms(
        @RequestAttribute("userId") userId: String,
        @RequestParam("sort") sort: ChatRoomSortCriteria
    ): ResponseEntity<HttpResponse<List<ChatRoomResponse>>> {
        val chatRooms = chatRoomFacade.getChatRooms(userId, sort)
        return ResponseHelper.success(chatRooms.map { ChatRoomResponse.from(it) })
    }

    // 채팅방 삭제
    @PostMapping("/delete")
    fun deleteChatRooms(
        @RequestBody request: ChatRoomRequest.Delete,
        @RequestAttribute("userId") userId: String
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        roomService.deleteChatRoom(request.chatRoomIds, userId)
        return ResponseHelper.successOnly()
    }

    @PostMapping("/delete/group")
    fun deleteGroupChatRooms(
        @RequestBody request: ChatRoomRequest.Delete,
        @RequestAttribute("userId") userId: String
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        chatRoomFacade.leavesChatRoom(request.chatRoomIds, userId)
        return ResponseHelper.successOnly()
    }

    // 채팅방 생성
    @PostMapping("/create")
    fun createChatRoom(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: ChatRoomRequest.Create
    ): SuccessResponseEntity<ChatRoomIdResponse> {
        val roomId = roomService.createChatRoom(userId, request.friendId)
        return ResponseHelper.success(ChatRoomIdResponse.from(roomId))
    }

    // 그룹 채팅방 생성
    @PostMapping("/create/group")
    fun createGroupChatRoom(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: ChatRoomRequest.CreateGroup
    ): SuccessResponseEntity<ChatRoomIdResponse> {
        val roomId = chatRoomFacade.createGroupChatRoom(userId, request.friendIds)
        return ResponseHelper.success(ChatRoomIdResponse.from(roomId))
    }
}