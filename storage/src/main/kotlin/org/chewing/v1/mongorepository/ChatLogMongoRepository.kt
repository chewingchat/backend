package org.chewing.v1.mongorepository

import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface ChatLogMongoRepository : MongoRepository<ChatMessageMongoEntity, String> {

    // 메시지 최신 페이지 계산
    @Query("{ 'chatRoomId': ?0 }")
    fun calculateLatestPage(chatRoomId: String): Int {
        val messageCount = countByRoomId(chatRoomId)
        return (messageCount / 20).toInt()  // 20개당 1페이지
    }



    @Query("{ 'chatRoomId': ?0, 'page': ?1 }")
    fun findByRoomIdAndPage(chatRoomId: String, page: Int): List<ChatMessageMongoEntity>

    // MongoDB에서 페이지별 메시지 수를 기반으로 카운트
    fun countByRoomId(chatRoomId: String): Long

    // 채팅방에서 최신 메시지를 가져오는 쿼리
    @Query("{ 'chatRoomId': ?0 }", sort = "{ 'timestamp': -1 }")
    fun findLatestMessageByRoomId(chatRoomId: String): ChatMessageMongoEntity?


    // 친구의 마지막 메시지 시퀀스를 조회하는 쿼리
    @Query("{ 'chatRoomId': ?0, 'senderId': ?1 }")
    fun findLastMessageByRoomIdAndFriendId(chatRoomId: String, friendId: Int): ChatMessageMongoEntity?


}


