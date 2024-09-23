package org.chewing.v1.jpaentity.emoticon

import jakarta.persistence.*
import org.chewing.v1.model.emoticon.Emoticon
import java.util.*

@Entity
@Table(name = "emoticon", schema = "chewing")
internal class EmoticonJpaEntity(
    @Id
    val emoticonId: String = UUID.randomUUID().toString(),
    val emoticonUrl: String,
    val emoticonName: String,
    val emoticonPackId: String,
) {
    companion object {
        fun fromEmoticon(emoticon: Emoticon, emoticonPackId: String): EmoticonJpaEntity {
            return EmoticonJpaEntity(
                emoticon.id,
                emoticon.media.url,
                emoticon.name,
                emoticonPackId
            )
        }
    }

    fun toEmoticon(): Emoticon {
        return Emoticon.of(
            emoticonId,
            emoticonName,
            emoticonUrl
        )
    }
}