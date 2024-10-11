package org.chewing.v1.service

import org.chewing.v1.model.*
import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.model.media.FileData
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,

) {

    fun getChatRooms(sort: String): List<ChatRoom> {
        return chatRoomRepository.getChatRooms(sort)
    }

    fun searchChatRooms(keyword: String): List<ChatRoom> {
        return chatRoomRepository.searchChatRooms(keyword)
    }

    fun deleteChatRooms(chatRoomIds: List<String>) {
        chatRoomRepository.deleteChatRooms(chatRoomIds)
    }

    fun getChatRoomInfo(chatRoomId: String): ChatRoom {
        return chatRoomRepository.getChatRoomInfo(chatRoomId)
    }

    fun getChatLogs(chatRoomId: String, page: Int): List<ChatLog> {
        return chatRoomRepository.getChatLogs(chatRoomId, page)
    }

    fun uploadChatRoomFiles(chatRoomId: String, fileDathList: List<FileData>) {
        if (fileDathList.isEmpty()) {
            throw IllegalArgumentException("파일이 존재하지 않습니다.")
        }

        chatRoomRepository.uploadChatRoomFiles(chatRoomId, fileDathList)
    }

    // 채팅방이 없을 경우 생성
    fun getOrCreateChatRoom(roomId: String, userId: String): ChatRoom {
        val chatRoom = chatRoomRepository.findByChatRoomId(roomId)
        return chatRoom ?: createChatRoom(roomId, userId)
    }

    // 채팅방 생성 로직
    private fun createChatRoom(roomId: String, userId: String): ChatRoom {
        val newChatRoom = ChatRoom(
            chatRoomId = roomId,
            favorite = false,
            groupChatRoom = false, // 기본적으로 단체 채팅이 아닌 것으로 설정
            latestMessage = "New chat room created",
            latestMessageTime = LocalDateTime.now().toString(),
            totalUnReadMessage = 0,
            chatFriends = listOf(), // 채팅방 생성 시 친구 목록 비어 있음
            0,
            1
        )
        chatRoomRepository.save(newChatRoom)
        return newChatRoom
    }


}


