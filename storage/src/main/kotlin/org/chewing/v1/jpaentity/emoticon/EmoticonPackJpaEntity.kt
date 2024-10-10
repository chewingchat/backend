package org.chewing.v1.jpaentity.emoticon

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.emoticon.EmoticonPack
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "emoticon_pack", schema = "chewing")
internal class EmoticonPackJpaEntity(
    @Id
    private val emoticonPackId: String = UUID.randomUUID().toString(),

    private val packUrl: String,

    private val packName: String,
) : BaseEntity() {
    fun toEmoticonPack(emoticons: List<Emoticon>): EmoticonPack {
        return EmoticonPack.of(
            emoticonPackId,
            packName,
            packUrl,
            emoticons
        )
    }
    fun getEmoticonId(): String {
        return emoticonPackId
    }
}