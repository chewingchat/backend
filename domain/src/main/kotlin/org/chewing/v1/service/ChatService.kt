package org.chewing.v1.service

import org.chewing.v1.implementation.chat.message.ChatAppender
import org.chewing.v1.implementation.chat.message.ChatReader
import org.chewing.v1.implementation.chat.message.ChatGenerator
import org.chewing.v1.implementation.chat.message.ChatRemover
import org.chewing.v1.implementation.chat.message.ChatSender
import org.chewing.v1.implementation.chat.room.ChatRoomReader
import org.chewing.v1.implementation.chat.room.ChatRoomUpdater
import org.chewing.v1.implementation.chat.sequence.ChatHelper
import org.chewing.v1.implementation.media.FileHandler
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.springframework.stereotype.Service


@Service
class ChatService(
    private val chatSender: ChatSender,
    private val fileHandler: FileHandler,
    private val chatHelper: ChatHelper,
    private val chatAppender: ChatAppender,
    private val chatReader: ChatReader,
    private val chatRoomReader: ChatRoomReader,
    private val chatRoomUpdater: ChatRoomUpdater,
    private val chatGenerator: ChatGenerator,
    private val chatRemover: ChatRemover,
) {
    fun uploadFiles(fileDataList: List<FileData>, userId: String, chatRoomId: String) {
        val medias = fileHandler.handleNewFiles(userId, fileDataList, FileCategory.CHATROOM)
        val chatRoomNumber = chatHelper.findNextNumber(chatRoomId)
        val chatMessage = chatGenerator.generateFileMessage(chatRoomId, userId, chatRoomNumber, medias)
        chatAppender.appendChatLog(chatMessage)
        val members = chatRoomReader.readUserInChatRooms(chatRoomId)
        chatSender.sendChat(chatMessage, members)
    }

    fun processRead(chatRoomId: String, userId: String) {
        val chatRoomNumber = chatHelper.findNextNumber(chatRoomId)
        chatHelper.findCurrentNumber(chatRoomId)
        val chatMessage = chatGenerator.generateReadMessage(chatRoomId, userId, chatRoomNumber)
        chatAppender.appendChatLog(chatMessage)
        chatRoomUpdater.updateRead(userId, chatRoomNumber)
        val members = chatRoomReader.readUserInChatRooms(chatRoomId)
        chatSender.sendChat(chatMessage, members)
    }

    fun processDelete(chatRoomId: String, userId: String, messageId: String) {
        val chatRoomNumber = chatHelper.findNextNumber(chatRoomId)
        val chatMessage = chatGenerator.generateDeleteMessage(chatRoomId, userId, chatRoomNumber, messageId)
        chatRemover.removeChatLog(messageId)
        val members = chatRoomReader.readUserInChatRooms(chatRoomId)
        chatSender.sendChat(chatMessage, members)
    }

    fun processReply(chatRoomId: String, userId: String, parentMessageId: String, text: String) {
        val chatRoomNumber = chatHelper.findNextNumber(chatRoomId)
        val parentMessage = chatReader.readChatMessage(parentMessageId)
        val chatMessage = chatGenerator.generateReplyMessage(chatRoomId, userId, chatRoomNumber, text, parentMessage)
        chatAppender.appendChatLog(chatMessage)
        val members = chatRoomReader.readUserInChatRooms(chatRoomId)
        chatSender.sendChat(chatMessage, members)
    }

    fun processChat(chatRoomId: String, userId: String, text: String) {
        val chatRoomNumber = chatHelper.findNextNumber(chatRoomId)
        val chatMessage = chatGenerator.generateCommonMessage(chatRoomId, userId, chatRoomNumber, text)
        chatAppender.appendChatLog(chatMessage)
        val members = chatRoomReader.readUserInChatRooms(chatRoomId)
        chatSender.sendChat(chatMessage, members)
    }


//    fun saveAndSendChat(chatMessage: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(chatMessage.chatRoomId!!)
//        val page = calculatePage(seq)
//        chatLogAppender.appendChatMessage(chatMessage, page)
//        chatSender.sendChat(chatMessage)
//    }
//
//    fun enterChatRoom(chatRoomId: String, userId: String) {
//        // 0. 채팅방이 존재하는지 확인하고 없으면 생성
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
//
//        // 4. 메시지를 기록하고 전송
////        chatLogAppender.appendChatMessage(enterMessage, calculatePage(seq))
////        chatSender.sendChat(enterMessage)
////
////        // 5. 읽음 처리 및 채팅 시퀀스 MongoDB에 저장
////        chatSequenceReader.saveChatSequence(chatRoomId, userId, seq)
////    }
//
//    // 채팅방 친구 목록과 읽은 시퀀스 번호를 조회하는 메서드 (DB 연동으로 수정)
//    fun getFriendsWithSeqNumber(chatRoomId: String): List<ChatMessage.FriendSeqInfo> {
//        // 실제 데이터베이스에서 친구 목록과 그들의 읽음 상태를 가져오는 로직으로 수정해야 합니다.
//        val friendsList = chatLogAppender.getChatFriends(chatRoomId)  // 친구 목록을 DB에서 가져옴
//        return friendsList.map { friend ->
//            val lastSeqNumber = chatSequenceReader.readFriendLastSeqNumber(chatRoomId, friend.friendId)
//            ChatMessage.FriendSeqInfo(friend.friendId, lastSeqNumber)
//        }
//    }

//    fun deleteChatMessage(chatRoomId: String, messageId: String) {
//        chatLogAppender.deleteMessage(chatRoomId, messageId)
//        val deleteMessage = ChatMessage.withoutId(
//            type = MessageType.DELETE,
//            chatRoomId = chatRoomId,
//            sender = "system",
//            messageSendType = MessageSendType(  // MessageSendType 인스턴스 생성
//                mediaType = null,  // DELETE 메시지 또한 TEXT로 처리
//                text = "Message has been deleted"
//            ),
//            parentMessageId = null,
//            parentMessagePage = null,
//            parentMessageSeqNumber = null,
//            friends = null,
//            page = 1
//        )
//        chatSender.sendChat(deleteMessage)
//    }

//    fun sendFileMessage(chatRoomId: String, fileDataList: List<FileData>) {
//        // 파일을 처리하여 Media로 변환
//        val mediaList = fileHandler.handleNewFiles(chatRoomId, fileDataList, FileCategory.FEED)
//
//        // 파일 메시지로 전송
//        mediaList.forEach { media ->
//            val message = ChatMessage.withoutId(
//                type = MessageType.FILE,
//                chatRoomId = chatRoomId,
//                sender = "system",  // 보낸 사람
//                messageSendType = MessageSendType(
//                    mediaType = media.type,
//                    text = media.url  // 파일 URL
//                ),
//                parentMessageId = null,
//                parentMessagePage = null,
//                parentMessageSeqNumber = null,
//                friends = null,
//                page = 1
//            )
//            chatLogAppender.appendChatMessage(message, 1)
//            chatSender.sendChat(message)
//        }
//    }

//    fun sendReplyMessage(chatMessage: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(chatMessage.chatRoomId!!)
//        val page = calculatePage(seq)
//
//        // REPLY 메시지 처리
//        chatLogAppender.appendChatMessage(chatMessage, page)
//        chatSender.sendChat(chatMessage)
//    }
//
//
//
//
//    fun leaveChatRoom(chatRoomId: String, userId: String) {
//        // LEAVE 메시지를 생성
//        val leaveMessage = ChatMessage.withoutId(
//            type = MessageType.LEAVE,
//            chatRoomId = chatRoomId,
//            sender = userId,
//            messageSendType = MessageSendType(  // MessageSendType 인스턴스 생성
//                mediaType = null,  // LEAVE 메시지도 TEXT로 처리
//                text = "User $userId has left the chat"
//            ),
//
//            parentMessageId = null,
//            parentMessagePage = null,
//            parentMessageSeqNumber = null,
//            friends = null,
//            page = 1
//        )
//        chatSender.sendChat(leaveMessage)
//        chatRoomRepository.deleteChatRooms(listOf(chatRoomId))
//    }

//
//    // 채팅방 입장 처리 (읽음 처리용)
//    fun processEnterMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.chatRoomId!!)
//        val page = calculatePage(seq)
//
//        val enterMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.IN,
//            chatRoomId = message.chatRoomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = null, text = "User entered"),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(enterMessage, page)
//        chatSender.sendChat(enterMessage)
//    }
//
//    // 단체 채팅방에서 입장 처리
//    fun processGroupEnterMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.chatRoomId!!)
//        val page = calculatePage(seq)
//
//        val enterGroupMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.ENTER,
//            chatRoomId = message.chatRoomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = null, text = "User entered group"),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(enterGroupMessage, page)
//        chatSender.sendChat(enterGroupMessage)
//    }
//
//    // 채팅방 밖에서 채팅 메시지 처리
//    fun processChatOutsideMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.chatRoomId!!)
//        val page = calculatePage(seq)
//
//        val chatOutsideMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.CHAT,
//            chatRoomId = message.chatRoomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = null, text = message.messageId),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(chatOutsideMessage, page)
//        chatSender.sendChat(chatOutsideMessage)
//    }
//
//    // 채팅방 밖에서 파일 메시지 처리
//    fun processFileOutsideMessage(message: ChatMessage) {
//        val seq = chatSequenceUpdater.updateSequenceIncrement(message.chatRoomId!!)
//        val page = calculatePage(seq)
//
//        val fileOutsideMessage = ChatMessage(
//            messageId = message.messageId ?: generateMessageId(),
//            type = MessageType.FILE,
//            chatRoomId = message.chatRoomId,
//            sender = message.sender,
//            messageSendType = message.messageSendType ?: MessageSendType(mediaType = message.messageSendType?.mediaType, text = message.messageSendType?.text),
//            parentMessageId = null,
//            parentMessagePage = message.parentMessagePage,
//            parentSeqNumber = message.parentSeqNumber,
//            timestamp = LocalDateTime.now(),
//            page = page
//        )
//        chatLogAppender.appendChatMessage(fileOutsideMessage, page)
//        chatSender.sendChat(fileOutsideMessage)
//    }
//
//
//
//
//    fun generateMessageId(): String {
//        return java.util.UUID.randomUUID().toString()
//    }
//
//
//
//
//
//    private fun calculatePage(seq: Long): Int {
//        return (seq / PAGE_SIZE).toInt()
//    }
//
//
//
//    companion object {
//        const val PAGE_SIZE = 50
//    }
}