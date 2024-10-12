package org.chewing.v1.service

import org.chewing.v1.implementation.chat.log.ChatLogAppender
import org.chewing.v1.implementation.chat.message.ChatMessageGenerator
import org.chewing.v1.implementation.chat.message.ChatSender
import org.chewing.v1.implementation.chat.room.*
import org.chewing.v1.implementation.chat.sequence.ChatHelper
import org.chewing.v1.implementation.media.FileHandler
import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.model.chat.MessageSendType
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.springframework.stereotype.Service

@Service
class ChatRoomService(
    private val chatRoomReader: ChatRoomReader,
    private val chatRoomRemover: ChatRoomRemover,
    private val chatRoomEnricher: ChatRoomEnricher,
    private val chatRoomAppender: ChatRoomAppender,
    private val chatRoomUpdater: ChatRoomUpdater,
    private val chatSender: ChatSender,
    private val chatHelper: ChatHelper,
) {
    fun getChatRooms(userId: String): List<ChatRoom> {
        // 중복된 map 호출을 변수로 저장
        val userChatRooms = chatRoomReader.readUserInChatRooms(userId)
        val chatRoomIds = userChatRooms.map { it.chatRoomId }

        val chatRoomMemberInfos = chatRoomReader.readChatRoomsMember(chatRoomIds, userId)

        val chatRoomInfos = chatRoomReader.readChatRooms(chatRoomMemberInfos.map { it.chatRoomId })
        // 채팅방 시퀀스 번호 조회
        val roomSequenceNumbers = chatHelper.readNumbers(chatRoomIds)

        // 채팅방을 풍부하게 채운 후 반환
        return chatRoomEnricher.enrichChatRooms(
            userChatRooms,
            chatRoomMemberInfos,
            chatRoomInfos,
            roomSequenceNumbers,
        )
    }

    fun deleteChatRooms(chatRoomIds: List<String>, userId: String) {
        val chatRoomUserInfo = chatRoomRemover.removeChatRoomsMember(chatRoomIds, userId)

//        chatSender.sendChat(chatRoomUserInfo)
    }

    fun createChatRoom(userId: String, friendId: String) {
        // 채팅방이 있는지 확인
        val chatRoomId = chatRoomReader.readPersonalChatRoomId(userId, friendId)
        if (chatRoomId == null) {
            // 없다면 생성
            val existRoomId = chatRoomAppender.appendChatRoom(false)
            chatRoomAppender.appendChatRoomMembers(existRoomId, listOf(userId, friendId))
        } else {
            // 있다면 내가 채팅방을 목록에서 삭제한 것임으로 복구처리
            chatRoomUpdater.updateUnDelete(chatRoomId, userId)
        }
    }

    fun createGroupChatRoom(userId: String, friendIds: List<String>) {
        val existRoomId = chatRoomAppender.appendChatRoom(true)
        chatRoomAppender.appendChatRoomMembers(existRoomId, friendIds)
    }

    /**
     * 초대를 생각 했는데 없으면 기능이 없는 것 같아용
     * */
//    fun enterChatRoom(roomId: String, userId: String) {
//        // 0. 채팅방에 가입
//        val chatRoom = chatRoomService.getOrCreateChatRoom(roomId, userId)
//
//        // 1. 시퀀스 번호 업데이트
//        val seq = chatSequenceUpdater.updateSequenceIncrement(chatRoom.chatRoomId)
//
//        // 2. 친구 목록과 각 친구의 읽은 시퀀스 번호를 가져오기
//        val friendsWithSeq = getFriendsWithSeqNumber(roomId)
//
//        // 3. 입장 메시지 생성
//        val enterMessage = ChatMessage.withoutId(
//            type = MessageType.IN,
//            roomId = roomId,
//            sender = userId,
//            messageSendType = MessageSendType(
//                mediaType = null,
//                text = "User $userId has entered the chat"
//            ),
//
//            parentMessageId = null,
//            parentMessagePage = null,
//            parentMessageSeqNumber = null,
//            friends = friendsWithSeq,
//            page = calculatePage(seq)
//        )
//    }

    // 4. 메시지를 기록하고 전송
//        chatLogAppender.appendChatMessage(enterMessage, calculatePage(seq))
//        chatSender.sendChat(enterMessage)
//
//        // 5. 읽음 처리 및 채팅 시퀀스 MongoDB에 저장
//        chatSequenceReader.saveChatSequence(roomId, userId, seq)
//    }

////    fun searchChatRooms(keyword: String): List<ChatRoom> {
////        return chatRoomRepository.searchChatRooms(keyword)
////    }
//
//    fun getChatRoom(chatRoomId: String): ChatRoom {
//        return chatRoomRepository.readChatRoom(chatRoomId)
//    }
//
//
//    // 채팅방이 없을 경우 생성
//    fun getOrCreateChatRoom(roomId: String, userId: String): ChatRoom {
//        val chatRoom = chatRoomRepository.findByChatRoomId(roomId)
//        return chatRoom ?: createChatRoom(roomId, userId)
//    }
//
//    // 채팅방 생성 로직
//    private fun createChatRoom(roomId: String, userId: String): ChatRoom {
//        val newChatRoom = ChatRoom(
//            chatRoomId = roomId,
//            favorite = false,
//            groupChatRoom = false, // 기본적으로 단체 채팅이 아닌 것으로 설정
//            latestMessage = "New chat room created",
//            latestMessageTime = LocalDateTime.now().toString(),
//            totalUnReadMessage = 0,
//            chatFriendInfos = listOf(), // 채팅방 생성 시 친구 목록 비어 있음
//            0,
//            1
//        )
//        chatRoomRepository.save(newChatRoom)
//        return newChatRoom
//    }
//
//
}