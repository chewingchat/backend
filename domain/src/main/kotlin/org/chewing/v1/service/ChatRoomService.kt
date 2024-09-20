package org.chewing.v1.service






import org.chewing.v1.model.chat.ChatLogResponse
import org.chewing.v1.model.chat.ChatRoomResponse
import org.chewing.v1.model.chat.FileUploadResponse
import java.io.File

interface ChatRoomService {

    fun getChatRooms(sort: String?): List<ChatRoomResponse>

    fun searchChatRooms(keyword: String): List<ChatRoomResponse>

    fun deleteChatRooms(chatRoomIds: List<String>)

    fun getChatRoomInfo(chatRoomId: String): ChatRoomResponse

    fun getChatLogs(chatRoomId: String, page: Int): ChatLogResponse

    fun uploadFiles(chatRoomId: String, files: List<File>): FileUploadResponse
}