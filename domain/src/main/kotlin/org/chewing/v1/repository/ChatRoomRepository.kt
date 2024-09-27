package org.chewing.v1.repository

import org.chewing.v1.model.ChatLog
import org.chewing.v1.model.ChatRoom
import org.chewing.v1.model.media.Media
import org.springframework.web.multipart.MultipartFile

interface ChatRoomRepository {

    // 채팅방 목록 가져오기
    fun getChatRooms(userId: String, sort: String): List<ChatRoom>

    // 채팅방 검색
    fun searchChatRooms(userId: String, keyword: String): List<ChatRoom>

    // 채팅방 삭제
    fun deleteChatRooms(userId: String, chatRoomIds: List<String>)

    // 채팅방 접속 후 정보 가져오기
    fun getChatRoomInfo(userId: String, chatRoomId: String): ChatRoom

    // 채팅 로그 가져오기
    fun getChatLogs(userId: String, chatRoomId: String, page: Int): List<ChatLog>

    // 채팅방 파일 업로드
    fun uploadChatRoomFiles(userId: String, chatRoomId: String, files: List<MultipartFile>)

    fun saveUploadedMedia(chatRoomId: String, mediaList: List<Media>)
}