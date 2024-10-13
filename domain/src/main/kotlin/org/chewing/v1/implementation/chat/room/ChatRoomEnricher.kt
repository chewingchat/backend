package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.ChatRoom
import org.chewing.v1.model.chat.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.chewing.v1.model.chat.room.ChatNumber
import org.springframework.stereotype.Component

@Component
class ChatRoomEnricher {

    fun enrichChatRooms(
        chatRoomMemberInfos: List<ChatRoomMemberInfo>,
        userChatRooms: List<ChatRoomMemberInfo>,
        chatRoomInfos: List<ChatRoomInfo>,
        roomSequenceNumbers: List<ChatNumber>
    ): List<ChatRoom> {
        val groupedChatRoomMembers = chatRoomMemberInfos.groupBy { it.chatRoomId }
        val userChatRoomsMap = userChatRooms.associateBy { it.chatRoomId }
        val roomSequenceNumberMap = roomSequenceNumbers.associateBy { it.chatRoomId }

        return chatRoomInfos.mapNotNull { chatRoomInfo ->
            // myChatRoom이 없는 경우 필터링
            val myChatRoom = userChatRoomsMap[chatRoomInfo.chatRoomId] ?: return@mapNotNull null

            val chatRoomMembers = groupedChatRoomMembers[chatRoomInfo.chatRoomId] ?: emptyList()
            val roomSequenceNumber = roomSequenceNumberMap[chatRoomInfo.chatRoomId]
            val seqNumber = roomSequenceNumber?.sequenceNumber ?: 0
            val page = roomSequenceNumber?.page ?: 0
            val totalUnRead = seqNumber - (myChatRoom.readSeqNumber)

            ChatRoom.of(
                chatRoomInfo = chatRoomInfo,
                userChatRoom = myChatRoom,
                chatRoomMembers = chatRoomMembers,
                totalUnReadMessage = totalUnRead,
                latestPage = page
            )
        }
    }
}
