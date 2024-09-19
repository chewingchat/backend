package org.chewing.v1.jpaentity.emoticon

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.emoticon.EmoticonPack
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "emoticon_pack", schema = "chewing")
class EmoticonPackJpaEntity(
    @Id
    val emoticonPackId: String = UUID.randomUUID().toString(),

    val emoticonPackUrl: String,

    val emoticonPackName: String,
) : BaseEntity() {

    companion object {
        fun fromEmoticonPack(emoticonPack: EmoticonPack): EmoticonPackJpaEntity {
            return EmoticonPackJpaEntity(
                emoticonPack.id,
                emoticonPack.media.url,
                emoticonPack.name
            )
        }
    }
}