package org.chewing.v1.controller

import org.chewing.v1.dto.request.chat.ChatRoomDeleteRequest
import org.chewing.v1.model.chat.ChatLogResponse
import org.chewing.v1.model.chat.ChatRoomResponse
import org.chewing.v1.model.chat.FileUploadResponse

import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.ChatRoomService
import org.chewing.v1.util.ResponseHelper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File

@RestController
@RequestMapping("/api/chatRooms")
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {

    @GetMapping
    fun getChatRooms(@RequestParam(required = false) sort: String?): ResponseEntity<List<ChatRoomResponse>> {
        val chatRooms = chatRoomService.getChatRooms(sort)
        return ResponseEntity.ok(chatRooms)
    }

    @GetMapping("/search")
    fun searchChatRooms(@RequestParam keyword: String): ResponseEntity<List<ChatRoomResponse>> {
        val chatRooms = chatRoomService.searchChatRooms(keyword)
        return ResponseEntity.ok(chatRooms)
    }

    @DeleteMapping
    fun deleteChatRooms(@RequestBody request: ChatRoomDeleteRequest): ResponseEntity<HttpResponse<SuccessOnlyResponse>> {
        chatRoomService.deleteChatRooms(request.chatRoomIds)
        return ResponseHelper.successOnly()
    }

    @GetMapping("/{chatRoomId}")
    fun getChatRoomInfo(@PathVariable chatRoomId: String): ResponseEntity<ChatRoomResponse> {
        val chatRoomInfo = chatRoomService.getChatRoomInfo(chatRoomId)
        return ResponseEntity.ok(chatRoomInfo)
    }

    @GetMapping("/{chatRoomId}/log/{page}")
    fun getChatLogs(@PathVariable chatRoomId: String, @PathVariable page: Int): ResponseEntity<ChatLogResponse> {
        val chatLogs = chatRoomService.getChatLogs(chatRoomId, page)
        return ResponseEntity.ok(chatLogs)
    }

    @PostMapping("/{chatRoomId}/file")
    fun uploadFiles(@PathVariable chatRoomId: String, @RequestParam("files") files: List<File>): ResponseEntity<FileUploadResponse> {
        val response = chatRoomService.uploadFiles(chatRoomId, files)
        return ResponseEntity.ok(response)
    }
}