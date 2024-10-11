package org.chewing.v1.repository

import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.springframework.web.multipart.MultipartFile

interface ChatRoomRepository {

    // 채팅방 목록 가져오기
    fun getChatRooms(sort: String): List<ChatRoom>

    // 채팅방 검색
    fun searchChatRooms(keyword: String): List<ChatRoom>

    // 채팅방 삭제
    fun deleteChatRooms(chatRoomIds: List<String>)

    // 채팅방 접속 후 정보 가져오기
    fun getChatRoomInfo(chatRoomId: String): ChatRoom

    // 채팅 로그 가져오기
    fun getChatLogs(chatRoomId: String, page: Int): List<ChatLog>

    // 채팅방 파일 업로드
    // 채팅방 파일 업로드
    fun uploadChatRoomFiles(chatRoomId: String, fileDathList: List<FileData>)

    fun saveUploadedMedia(chatRoomId: String, mediaList: List<Media>)

    fun findByChatRoomId(roomId: String): ChatRoom?

    fun save(chatRoom: ChatRoom)


}