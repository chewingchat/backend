//package org.chewing.v1.controller
//
//import org.chewing.v1.dto.response.chat.ChatLogDto
//import org.chewing.v1.response.HttpResponse
//import org.chewing.v1.service.ChatService
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RequestParam
//import org.springframework.web.bind.annotation.RestController
//
//
//@RestController
//@RequestMapping("/api/chatLog")
//class ChatLogController(private val chatService: ChatService) {
//
//    @GetMapping
//    fun getChatLog(@RequestParam roomId: String, @RequestParam page: Int): HttpResponse<ChatLogDto> {
//        val chatLog = chatService.getChatLog(roomId, page)
//        return HttpResponse.success(ChatLogDto.from(chatLog))
//    }
//
//    @GetMapping("/latest")
//    fun getChatLogLatest(@RequestParam roomId: String): HttpResponse<ChatLogDto> {
//        val chatLog = chatService.getChatLogLatest(roomId)
//        return HttpResponse.success(ChatLogDto.from(chatLog))
//    }
//}