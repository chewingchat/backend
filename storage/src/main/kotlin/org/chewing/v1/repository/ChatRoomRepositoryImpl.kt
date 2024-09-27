package org.chewing.v1.repository

import org.chewing.v1.model.*
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.error.UnauthorizedException
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.model.chat.ChatFriend
import org.chewing.v1.model.chat.ChatLog
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.model.media.Media
import org.chewing.v1.util.FileUtil
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.util.*


@Repository
class ChatRoomRepositoryImpl(
    private val fileProcessor: FileProcessor, // FileProcessor 주입

) : ChatRoomRepository {
    private val chatRooms = mutableListOf<ChatRoom>()
    private val chatLogs = mutableMapOf<String, MutableList<ChatLog>>()
    override fun getChatRooms(userId: String, sort: String): List<ChatRoom> {


        val sortedChatRooms = when (sort) {
            "favorite" -> chatRooms.filter { it.isFavorite }
            "latest" -> chatRooms.sortedByDescending { it.latestMessageTime }
            "notRead" -> chatRooms.filter { it.totalUnReadMessage > 0 }
            else -> throw IllegalArgumentException("Invalid sort parameter")
        }

        if (sortedChatRooms.isEmpty()) throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
        return sortedChatRooms
    }

    override fun searchChatRooms(userId: String, keyword: String): List<ChatRoom> {

        // 각 채팅방의 최신 메시지, 친구 기준으로 검색
        // JPA로 넘기는게 좋겠죠? 어차피 3번 나눠야하니...
        val filteredRooms = chatRooms.filter { chatRoom ->
            chatRoom.latestMessage.contains(keyword) ||
                    chatRoom.chatFriends.any { friend ->
                        "${friend.friendFirstName} ${friend.friendLastName}".contains(keyword)
                    }
        }

        if (filteredRooms.isEmpty()) throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
        return filteredRooms
    }

    override fun deleteChatRooms(userId: String, chatRoomIds: List<String>) {
        chatRoomIds.forEach { chatRoomId ->
            val roomToDelete = chatRooms.find { it.chatRoomId == chatRoomId }
                ?: throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)

            chatRooms.remove(roomToDelete)
        }
    }

    override fun getChatRoomInfo(userId: String, chatRoomId: String): ChatRoom {

        val chatRoom = chatRooms.find { it.chatRoomId == chatRoomId }
            ?: throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
        // 최신 페이지와 친구가 읽은 시퀀스 번호 계산
        val latestPage = (chatLogs[chatRoomId]?.size ?: 0) / 20
        val friendReadSeqNumber = calculateFriendReadSeqNumber(chatRoom.chatFriends, chatRoomId)

        return chatRoom.copy(
            latestPage = latestPage,
            friendReadSeqNumber = friendReadSeqNumber
        )
    }

    override fun getChatLogs(userId: String, chatRoomId: String, page: Int): List<ChatLog> {
        val logs = chatLogs[chatRoomId]?.filter { it.page == page }
            ?: throw NotFoundException(ErrorCode.CHATLOG_NOT_FOUND)

        // 답글의 부모 메시지 정보 추가
        return logs.map { log ->
            if (log.type == "REPLY") {
                val parentMessage = findParentMessage(log.parentMessageId ?: "")
                log.copy(
                    parentMessage = parentMessage?.message,
                    parentMessagePage = parentMessage?.page,
                    parentMessageSeqNumber = parentMessage?.messageSeqNumber
                )
            } else {
                log
            }
        }
    }

    override fun uploadChatRoomFiles(userId: String, chatRoomId: String, files: List<MultipartFile>) {
        try {
            // MultipartFile을 File로 변환
            val convertedFiles: List<File> = FileUtil.convertMultipartFilesToFiles(files)

            // 변환된 파일들을 처리하여 미디어로 업로드
            val uploadedMedia: List<Media> = fileProcessor.processNewFiles(userId, convertedFiles)

            // 업로드된 미디어를 채팅방에 연결하는 로직
            saveUploadedMedia(chatRoomId, uploadedMedia)

        } catch (e: IOException) {
            throw IllegalArgumentException("파일 변환에 실패했습니다: ${e.message}")
        }
    }


    override fun saveUploadedMedia(chatRoomId: String, mediaList: List<Media>) {
        // 채팅방을 조회하여 존재하는지 확인
        val chatRoom = chatRooms.find { it.chatRoomId == chatRoomId }
            ?: throw NotFoundException(ErrorCode.CHATROOM_NOT_FOUND)

        // 채팅방의 기존 미디어 또는 로그에 업로드된 미디어를 추가
        mediaList.forEach { media ->
            val newChatLog = ChatLog(
                type = media.type.name,  // 미디어 타입 (예: IMAGE, VIDEO)
                messageId = UUID.randomUUID().toString(),  // 고유 메시지 ID
                senderId = "system",  // 파일 업로드는 시스템 또는 특정 사용자로 처리
                message = media.url,  // 업로드된 파일의 URL
                messageSendTime = LocalDateTime.now().toString(),  // 메시지 전송 시간
                messageSeqNumber = chatLogs[chatRoomId]?.size ?: 0,  // 메시지 시퀀스 넘버
                page = (chatLogs[chatRoomId]?.size ?: 0) / 20  // 페이지 번호 (예: 20개당 1페이지)
            )

            // 채팅방의 채팅 로그에 새로운 로그 추가
            chatLogs.computeIfAbsent(chatRoomId) { mutableListOf() }.add(newChatLog)
        }
    }

    private fun calculateFriendReadSeqNumber(friends: List<ChatFriend>, chatRoomId: String): Int {
        // 친구들이 읽은 마지막 메시지를 찾는 로직
        return chatLogs[chatRoomId]?.lastOrNull()?.messageSeqNumber ?: 0
    }

    private fun findParentMessage(parentMessageId: String): ChatLog? {
        // 모든 채팅 로그에서 부모 메시지 ID로 검색
        return chatLogs.values.flatten().find { it.messageId == parentMessageId }
    }

}