package org.chewing.v1.controller

import org.chewing.v1.dto.request.chat.DeleteChatRoomRequest
import org.chewing.v1.model.*
import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.ChatRoomService
import org.chewing.v1.util.ResponseHelper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/chatRooms")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {

    // 채팅방 목록 가져오기
    @GetMapping
    fun getChatRooms(
        @RequestAttribute("userId") userId: String,
        @RequestParam("sort") sort: String
    ): ResponseEntity<HttpResponse<List<ChatRoom>>> {
        val chatRooms = chatRoomService.getChatRooms(userId, sort)
        return ResponseHelper.success(chatRooms)
    }

    // 채팅방 검색
    @GetMapping("/search")
    fun searchChatRooms(
        @RequestAttribute("userId") userId: String,
        @RequestParam("keyword") keyword: String
    ): ResponseEntity<HttpResponse<List<ChatRoom>>> {
        val chatRooms = chatRoomService.searchChatRooms(userId, keyword)
        return ResponseHelper.success(chatRooms)
    }

    // 채팅방 삭제
    @DeleteMapping
    fun deleteChatRooms(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: DeleteChatRoomRequest
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        chatRoomService.deleteChatRooms(userId, request.chatRoomIds)
        return ResponseHelper.successOnly()
    }

    // 채팅방 접속 후 정보 가져오기
    @GetMapping("/{chatRoomId}")
    fun getChatRoomInfo(
        @RequestAttribute("userId") userId: String,
        @PathVariable chatRoomId: String
    ): ResponseEntity<HttpResponse<ChatRoom>> {
        val chatRoom = chatRoomService.getChatRoomInfo(userId, chatRoomId)
        return ResponseHelper.success(chatRoom)
    }

    // 채팅 로그 가져오기
    @GetMapping("/{chatRoomId}/log/{page}")
    fun getChatLogs(
        @RequestAttribute("userId") userId: String,
        @PathVariable chatRoomId: String,
        @PathVariable page: Int
    ): ResponseEntity<HttpResponse<List<ChatLog>>> {
        val chatLogs = chatRoomService.getChatLogs(userId, chatRoomId, page)
        return ResponseHelper.success(chatLogs)
    }

    // 채팅방 파일 업로드
    @PostMapping("/{chatRoomId}/file")
    fun uploadChatRoomFiles(
        @RequestAttribute("userId") userId: String,
        @PathVariable chatRoomId: String,
        @RequestParam("files") files: List<MultipartFile>
    ): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        chatRoomService.uploadChatRoomFiles(userId, chatRoomId, files)
        return ResponseHelper.successOnly()
    }
}