package org.chewing.v1.repository.chat

import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatNumber

interface ChatRoomMemberRepository {
    fun saveChatRoomMember(chatRoomId: String, userId: String)
    fun changeChatRoomFavorite(chatRoomId: String, userId: String, isFavorite: Boolean)
    fun readMembersByUserId(userId: String): List<ChatRoomMemberInfo>
    fun removeChatRoomMembers(chatRoomIds: List<String>,userId: String)
    fun appendChatRoomMember(chatRoomId: String, userId: String)
    fun readPersonalChatRoomId(userId: String, friendId: String): String?

    fun appendChatRoomMembers(chatRoomId: String, userIds: List<String>)
    fun updateUnDelete(chatRoomId: String, userId: String)
    fun updateRead(userId: String, number: ChatNumber)
    fun readChatRoomFriendMember(chatRoomId: String, userId: String): List<ChatRoomMemberInfo>
}