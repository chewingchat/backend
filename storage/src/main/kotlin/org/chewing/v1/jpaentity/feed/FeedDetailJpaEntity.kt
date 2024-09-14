package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.media.MediaType.*
import org.chewing.v1.model.media.Video
import java.util.*

@Entity
@Table(name = "feed_detail", schema = "chewing")
class FeedDetailJpaEntity(
    @Id
    val feedDetailId: String = UUID.randomUUID().toString(),
    val feedIndex: Int,
    private val feedDetailUrl: String,
    @Enumerated(EnumType.STRING)
    private val feedDetailType: MediaType,
) {
    companion object {
        fun fromFeedDetail(feedDetail: FeedDetail): FeedDetailJpaEntity {
            return FeedDetailJpaEntity(
                feedDetailId = feedDetail.id,
                feedIndex = feedDetail.media.index,
                feedDetailUrl = feedDetail.media.url,
                feedDetailType = feedDetail.media.type
            )
        }
    }

    fun toFeedDetail(): FeedDetail {
        return when (feedDetailType) {
            IMAGE -> FeedDetail.of(
                feedDetailId = feedDetailId,
                media = Image.of(feedDetailUrl, feedIndex)
            )

            VIDEO -> FeedDetail.of(
                feedDetailId = feedDetailId,
                media = Video.of(feedDetailUrl, feedIndex)
            )
        }
    }
}
