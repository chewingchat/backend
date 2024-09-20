package org.chewing.v1.repository


import org.chewing.v1.model.chat.ChatLogResponse
import org.chewing.v1.model.chat.ChatRoomResponse
import org.chewing.v1.model.chat.FileUploadResponse
import org.chewing.v1.service.ChatRoomService
import org.springframework.stereotype.Service
import java.io.File


@Service
class ChatRoomServiceImpl(
    private val chatRoomRepository: ChatRoomRepository,

) : ChatRoomService {

    override fun getChatRooms(sort: String?): List<ChatRoomResponse> {
        return chatRoomRepository.findChatRooms(sort)
    }

    override fun searchChatRooms(keyword: String): List<ChatRoomResponse> {
        return chatRoomRepository.searchChatRooms(keyword)
    }

    override fun deleteChatRooms(chatRoomIds: List<String>) {
        chatRoomRepository.deleteChatRooms(chatRoomIds)
    }

    override fun getChatRoomInfo(chatRoomId: String): ChatRoomResponse {
        return chatRoomRepository.findChatRoomInfo(chatRoomId)
    }

    override fun getChatLogs(chatRoomId: String, page: Int): ChatLogResponse {
        return chatRoomRepository.findChatLogs(chatRoomId, page)
    }

    override fun uploadFiles(chatRoomId: String, files: List<File>): FileUploadResponse {
        return chatRoomRepository.uploadFiles(chatRoomId, files)
    }
}