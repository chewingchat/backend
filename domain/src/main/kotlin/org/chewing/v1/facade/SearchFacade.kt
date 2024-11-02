package org.chewing.v1.facade

import org.chewing.v1.implementation.chat.room.ChatRoomAggregator
import org.chewing.v1.implementation.chat.room.ChatRoomSortEngine
import org.chewing.v1.implementation.search.FriendSearchEngine
import org.chewing.v1.model.chat.room.ChatRoomSortCriteria
import org.chewing.v1.model.friend.FriendSortCriteria
import org.chewing.v1.model.search.Search
import org.chewing.v1.service.chat.ChatLogService
import org.chewing.v1.service.chat.RoomService
import org.chewing.v1.service.friend.FriendShipService
import org.springframework.stereotype.Service

@Service
class SearchFacade(
    private val friendShipService: FriendShipService,
    private val roomService: RoomService,
    private val chatLogService: ChatLogService,
    private val chatRoomAggregator: ChatRoomAggregator,
    private val friendSearchEngine: FriendSearchEngine,
) {
    fun search(userId: String, keyword: String): Search {
        val friendShips = friendShipService.getAccessFriendShips(userId, FriendSortCriteria.NAME)
        val searchedFriendShips = friendSearchEngine.search(friendShips, keyword)
        val roomInfos = roomService.getChatRooms(userId)
        val chatNumbers = chatLogService.getLatestChat(roomInfos.map { it.chatRoomId })
        val chatRoomInfos = chatRoomAggregator.aggregateChatRoom(roomInfos, chatNumbers)
        val chatRooms = ChatRoomSortEngine.sortChatRoom(chatRoomInfos, ChatRoomSortCriteria.DATE)
        return Search.of(chatRooms, searchedFriendShips)
    }
}