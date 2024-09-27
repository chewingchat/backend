package org.chewing.v1.service

import org.chewing.v1.model.*
import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository
) {

    fun getChatRooms(userId: String, sort: String): List<ChatRoom> {
        return chatRoomRepository.getChatRooms(userId, sort)
    }

    fun searchChatRooms(userId: String, keyword: String): List<ChatRoom> {
        return chatRoomRepository.searchChatRooms(userId, keyword)
    }

    fun deleteChatRooms(userId: String, chatRoomIds: List<String>) {
        chatRoomRepository.deleteChatRooms(userId, chatRoomIds)
    }

    fun getChatRoomInfo(userId: String, chatRoomId: String): ChatRoom {
        return chatRoomRepository.getChatRoomInfo(userId, chatRoomId)
    }

    fun getChatLogs(userId: String, chatRoomId: String, page: Int): List<ChatLog> {
        return chatRoomRepository.getChatLogs(userId, chatRoomId, page)
    }

    fun uploadChatRoomFiles(userId: String, chatRoomId: String, files: List<MultipartFile>) {
        if (files.isEmpty()) {
            throw IllegalArgumentException("파일이 존재하지 않습니다.")
        }

        chatRoomRepository.uploadChatRoomFiles(userId, chatRoomId, files)
    }


}