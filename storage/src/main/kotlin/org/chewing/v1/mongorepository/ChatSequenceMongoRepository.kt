package org.chewing.v1.mongorepository

import org.chewing.v1.model.chat.ChatFriendInfo
import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.chewing.v1.mongoentity.ChatSequenceMongoEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChatSequenceMongoRepository : MongoRepository<ChatSequenceMongoEntity, String> {
    // 친구가 읽은 마지막 시퀀스 번호 계산
    @Query("{ 'roomId': ?0, 'friends.friendId': ?1 }")
    fun calculateFriendReadSeqNumber(roomId: Int, chatFriendInfos: List<ChatFriendInfo>): Int {
        var highestSeqNumber = 0  // 가장 높은 seqNumber를 저장할 변수

        for (friend in chatFriendInfos) {
            // 각 친구가 마지막으로 읽은 메시지를 가져옴
            val lastReadMessage = findIdOrderBySeqNumberDesc(roomId, friend.friendId)

            // lastReadMessage가 null이 아니고, seqNumber가 null이 아니며 해당 메시지의 seqNumber가 현재 highestSeqNumber보다 크면 업데이트
            val lastSeqNumber = lastReadMessage?.seqNumber
            if (lastSeqNumber != null && lastSeqNumber > highestSeqNumber) {
                highestSeqNumber = lastSeqNumber
            }
        }

        return highestSeqNumber  // 모든 친구들 중 가장 높은 seqNumber 반환
    }
    // 친구가 읽은 마지막 시퀀스 번호 계산
    @Query("{ 'roomId': ?0, 'friends.friendId': ?1 }")
    fun findIdOrderBySeqNumberDesc(roomId: Int, friendId: Int): ChatMessageMongoEntity?
}