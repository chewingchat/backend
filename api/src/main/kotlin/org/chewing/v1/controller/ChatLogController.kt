package org.chewing.v1.controller

import org.chewing.v1.dto.response.chat.ChatLogResponse
import org.chewing.v1.response.HttpResponse
import org.chewing.v1.service.ChatLogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/chatLog")
class ChatLogController(private val chatLogService: ChatLogService) {

    @GetMapping
    fun getChatLog(@RequestParam roomId: String, @RequestParam page: Int): HttpResponse<ChatLogResponse> {
        val chatLog = chatLogService.getChatLog(roomId, page)
        return HttpResponse.success(ChatLogResponse.from(chatLog))
    }

    @GetMapping("/latest")
    fun getChatLogLatest(@RequestParam roomId: String): HttpResponse<ChatLogResponse> {
        val chatLog = chatLogService.getChatLogLatest(roomId)
        return HttpResponse.success(ChatLogResponse.from(chatLog))
    }
}