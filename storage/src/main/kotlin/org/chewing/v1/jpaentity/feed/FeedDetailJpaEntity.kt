package org.chewing.v1.jpaentity.feed

import jakarta.persistence.*
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.Media
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
    val feedId: String,
) {
    companion object {
        fun generate(medias: List<Media>, feedId: Feed.FeedId): List<FeedDetailJpaEntity> {
            return medias.map { media ->
                FeedDetailJpaEntity(
                    feedIndex = media.index,
                    feedDetailUrl = media.url,
                    feedDetailType = media.type,
                    feedId = feedId.value()
                )
            }
        }
    }
    fun toDetailId(): String {
        return feedDetailId
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
