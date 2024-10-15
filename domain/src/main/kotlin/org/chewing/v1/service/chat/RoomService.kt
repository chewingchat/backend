package org.chewing.v1.service.chat

import org.chewing.v1.implementation.chat.room.*
import org.chewing.v1.model.chat.room.Room
import org.springframework.stereotype.Service

@Service
class RoomService(
    private val chatRoomReader: ChatRoomReader,
    private val chatRoomRemover: ChatRoomRemover,
    private val chatRoomEnricher: ChatRoomEnricher,
    private val chatRoomAppender: ChatRoomAppender,
    private val chatRoomHandler: ChatRoomHandler,
) {
    fun getChatRooms(userId: String): List<Room> {
        val chatRoomMembers = chatRoomReader.readChatRoomMembersByUserId(userId)
        val chatRoomInfos = chatRoomReader.reads(chatRoomMembers.map { it.chatRoomId })
        return chatRoomEnricher.enrichChatRooms(
            chatRoomMembers,
            chatRoomInfos,
            userId
        )
    }

    fun deleteGroupChatRooms(chatRoomIds: List<String>, userId: String) {
        chatRoomRemover.removeMembers(chatRoomIds, userId)
    }

    fun deleteChatRoom(chatRoomIds: List<String>, userId: String) {
        chatRoomRemover.removeMembers(chatRoomIds, userId)
    }

    fun createChatRoom(userId: String, friendId: String): String {
        val chatRoomId = chatRoomReader.readPersonalChatRoomId(userId, friendId)
        return if (chatRoomId == null) {
            val newRoomId = chatRoomAppender.append(false)
            chatRoomAppender.appendMembers(newRoomId, listOf(userId, friendId))
            newRoomId
        } else {
            chatRoomHandler.lockActivateChatRoomUser(chatRoomId, userId)
            chatRoomId
        }
    }

    fun createGroupChatRoom(userId: String, friendIds: List<String>): String {
        val newRoomId = chatRoomAppender.append(true)
        chatRoomAppender.appendMembers(newRoomId, friendIds)
        return newRoomId
    }


    /**
     * 초대를 생각 했는데 없으면 기능이 없는 것 같아용
     * */
//    fun enterChatRoom(chatRoomId: String, userId: String) {
//        // 0. 채팅방에 가입
//        val chatRoom = chatRoomService.getOrCreateChatRoom(chatRoomId, userId)
//
//        // 1. 시퀀스 번호 업데이트
//        val seq = chatSequenceUpdater.updateSequenceIncrement(chatRoom.chatRoomId)
//
//        // 2. 친구 목록과 각 친구의 읽은 시퀀스 번호를 가져오기
//        val friendsWithSeq = getFriendsWithSeqNumber(chatRoomId)
//
//        // 3. 입장 메시지 생성
//        val enterMessage = ChatMessage.withoutId(
//            type = MessageType.IN,
//            chatRoomId = chatRoomId,
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
//        chatSequenceReader.saveChatSequence(chatRoomId, userId, seq)
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
//    fun getOrCreateChatRoom(chatRoomId: String, userId: String): ChatRoom {
//        val chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
//        return chatRoom ?: createChatRoom(chatRoomId, userId)
//    }
//
//    // 채팅방 생성 로직
//    private fun createChatRoom(chatRoomId: String, userId: String): ChatRoom {
//        val newChatRoom = ChatRoom(
//            chatRoomId = chatRoomId,
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
}