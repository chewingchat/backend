package org.chewing.v1.dto.request.chat

class ChatRoomRequest{
    data class Create(
        val friendId: String = ""
    )
    data class CreateGroup(
        val friendIds: List<String> = emptyList(),
    )
    data class Delete(
        val chatRoomIds: List<String> = emptyList()
    )
}