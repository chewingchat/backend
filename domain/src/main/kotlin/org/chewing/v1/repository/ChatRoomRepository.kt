package org.chewing.v1.repository

import org.chewing.v1.model.*
import org.chewing.v1.model.chat.ChatLogResponse
import org.chewing.v1.model.chat.ChatRoomResponse
import org.chewing.v1.model.chat.FileUploadResponse

import org.springframework.stereotype.Repository
import java.io.File

@Repository
interface ChatRoomRepository {

    fun findChatRooms(sort: String?): List<ChatRoomResponse>

    fun searchChatRooms(keyword: String): List<ChatRoomResponse>

    fun deleteChatRooms(chatRoomIds: List<String>)

    fun findChatRoomInfo(chatRoomId: String): ChatRoomResponse

    fun findChatLogs(chatRoomId: String, page: Int): ChatLogResponse

    fun uploadFiles(chatRoomId: String, files: List<File>): FileUploadResponse
}