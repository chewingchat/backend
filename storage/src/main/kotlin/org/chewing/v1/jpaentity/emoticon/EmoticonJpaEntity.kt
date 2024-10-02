package org.chewing.v1.jpaentity.emoticon

import jakarta.persistence.*
import org.chewing.v1.model.emoticon.Emoticon
import java.util.*

@Entity
@Table(name = "emoticon", schema = "chewing")
internal class EmoticonJpaEntity(
    @Id
    private val emoticonId: String = UUID.randomUUID().toString(),
    private val emoticonUrl: String,
    private val emoticonName: String,
    private val emoticonPackId: String,
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

    fun getEmoticonId(): String {
        return emoticonId
    }
}