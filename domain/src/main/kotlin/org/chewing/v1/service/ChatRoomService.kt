package org.chewing.v1.service

import org.chewing.v1.implementation.chat.message.ChatAppender
import org.chewing.v1.implementation.chat.message.ChatGenerator
import org.chewing.v1.implementation.chat.message.ChatSender
import org.chewing.v1.implementation.chat.room.*
import org.chewing.v1.implementation.chat.sequence.ChatFinder
import org.chewing.v1.model.chat.room.ChatRoom
import org.springframework.stereotype.Service

@Service
class ChatRoomService(
    private val chatRoomReader: ChatRoomReader,
    private val chatRoomRemover: ChatRoomRemover,
    private val chatRoomEnricher: ChatRoomEnricher,
    private val chatRoomAppender: ChatRoomAppender,
    private val chatRoomUpdater: ChatRoomUpdater,
    private val chatGenerator: ChatGenerator,
    private val chatAppender: ChatAppender,
    private val chatSender: ChatSender,
    private val chatFinder: ChatFinder,
) {
    fun getChatRooms(userId: String): List<ChatRoom> {
        val userChatRooms = chatRoomReader.readUserInChatRooms(userId)
        val chatRoomIds = userChatRooms.map { it.chatRoomId }
        val chatRoomMemberInfos = chatRoomReader.readChatRoomsMember(chatRoomIds, userId)
        val chatRoomInfos = chatRoomReader.readChatRooms(chatRoomMemberInfos.map { it.chatRoomId })
        val roomSequenceNumbers = chatFinder.findCurrentNumbers(chatRoomIds)

        return chatRoomEnricher.enrichChatRooms(
            userChatRooms,
            chatRoomMemberInfos,
            chatRoomInfos,
            roomSequenceNumbers,
            userId
        )
    }

    fun leaveChatRooms(chatRoomIds: List<String>, userId: String) {
        chatRoomRemover.removeChatRoomsMember(chatRoomIds, userId)

        val members = chatRoomReader.readUserInChatRooms(userId)

        chatFinder.findNextNumbers(chatRoomIds).forEach { number ->
            val message = chatGenerator.generateLeaveMessage(number.chatRoomId, userId, number)
            chatAppender.appendChatLog(message)
            chatSender.sendChat(message, members)
        }
    }

    fun createChatRoom(userId: String, friendId: String) {
        val chatRoomId = chatRoomReader.readPersonalChatRoomId(userId, friendId)
        if (chatRoomId == null) {
            val newRoomId = chatRoomAppender.appendChatRoom(false)
            chatRoomAppender.appendChatRoomMembers(newRoomId, listOf(userId, friendId))
        } else {
            chatRoomUpdater.updateUnDelete(chatRoomId, userId)
        }
    }

    fun createGroupChatRoom(userId: String, friendIds: List<String>) {
        val newRoomId = chatRoomAppender.appendChatRoom(true)
        chatRoomAppender.appendChatRoomMembers(newRoomId, friendIds)

        val members = chatRoomReader.readUserInChatRooms(userId)

        friendIds.forEach { friendId ->
            val number = chatFinder.findNextNumber(newRoomId)
            val message = chatGenerator.generateInviteMessage(newRoomId, userId, number, friendId)
            chatAppender.appendChatLog(message)
            chatSender.sendChat(message, members)
        }
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