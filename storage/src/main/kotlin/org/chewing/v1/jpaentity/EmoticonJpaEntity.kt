package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.Emoticon
import org.hibernate.annotations.DynamicInsert
import java.util.*

@Entity
@Table(name = "emoticon", schema = "chewing")
class EmoticonJpaEntity(
    @Id
    @Column(name = "emoticon_id")
    val emoticonId: String = UUID.randomUUID().toString(),

    @Column(name = "emoticon_url", nullable = false)
    val emoticonUrl: String,

    @Column(name = "emoticon_name", nullable = false)
    val emoticonName: String,
) {
    companion object {
        fun fromEmoticon(emoticon: Emoticon): EmoticonJpaEntity {
            return EmoticonJpaEntity(
                emoticon.emoticonId,
                emoticon.emoticonUrl,
                emoticon.emoticonName,
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