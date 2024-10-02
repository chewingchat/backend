package org.chewing.v1.repository

import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.user.User
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedOwner
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository {
    fun read(feedId: String): FeedInfo?
    fun reads(feedIds: List<String>): List<FeedInfo>
    fun readsByUserId(userId: String,feedOwner: FeedOwner): List<FeedInfo>
    fun readDetails(feedId: String): List<FeedDetail>
    fun readsDetails(feedIds: List<String>):  List<FeedDetail>
    fun readsByOwner(feedIds: List<String>, userIds:List<String>): List<FeedInfo>
    fun isAllOwner(feedIds: List<String>, userId: String): Boolean
    fun removes(feedIds: List<String>)
    fun removesDetails(feedIds: List<String>): List<Media>
    fun append(medias: List<Media>,userId: String, topic: String): String
    fun update(feedId: String, target: FeedTarget)
}