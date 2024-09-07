package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.media.Video
import java.util.*

@Entity
@Table(name = "feed_detail", schema = "chewing")
class FeedDetailJpaEntity(
    @Id
    @Column(name = "feed_detail_id")
    val feedDetailId: String = UUID.randomUUID().toString(),

    @Column(name = "feed_index", nullable = false)
    val index: Int,

    @Column(name = "feed_detail_url", nullable = false)
    private val feedDetailUrl: String,

    @Column(name = "feed_detail_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private val feedDetailType: MediaType,
) {
    companion object {
        fun fromFeedDetail(feedDetail: FeedDetail): FeedDetailJpaEntity {
            return FeedDetailJpaEntity(
                feedDetailId = feedDetail.feedDetailId,
                index = feedDetail.index,
                feedDetailUrl = feedDetail.media.url,
                feedDetailType = feedDetail.media.type
            )
        }
    }

    fun toFeedDetail(): FeedDetail {
        return when (feedDetailType) {
            MediaType.IMAGE -> FeedDetail.of(
                feedDetailId = feedDetailId,
                index = index,
                media = Image.of(feedDetailUrl)
            )

            MediaType.VIDEO -> FeedDetail.of(
                feedDetailId = feedDetailId,
                index = index,
                media = Video.of(feedDetailUrl)
            )
        }
    }
}
