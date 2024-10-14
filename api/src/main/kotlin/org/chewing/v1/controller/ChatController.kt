package org.chewing.v1.controller

import org.chewing.v1.dto.response.chat.ChatLogResponse
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.service.ChatService
import org.chewing.v1.util.FileUtil
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService
) {
    // 파일 업로드
    @PostMapping("/file/upload")
    fun uploadFiles(
        @RequestPart("files") files: List<MultipartFile>,
        @RequestAttribute("userId") userId: String,
        @RequestParam("chatRoomId") chatRoomId: String
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val convertFiles = FileUtil.convertMultipartFileToFileDataList(files)
        chatService.uploadFiles(convertFiles, userId, chatRoomId)
        //생성 완료 응답 201 반환
        return ResponseHelper.successCreate()
    }

    @GetMapping("/log")
    fun getChatLog(@RequestParam chatRoomId: String, @RequestParam page: Int): HttpResponse<ChatLogResponse> {
        val chatLog = chatService.getChatLog(chatRoomId, page)
        return HttpResponse.success(ChatLogResponse.from(chatLog))
    }

    @GetMapping("/log/latest")
    fun getChatLogLatest(@RequestParam chatRoomId: String): HttpResponse<ChatLogResponse> {
        val chatLog = chatService.getChatLogLatest(chatRoomId)
        return HttpResponse.success(ChatLogResponse.from(chatLog))
    }
}