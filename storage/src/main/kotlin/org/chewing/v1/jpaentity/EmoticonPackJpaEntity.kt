package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.common.BaseEntity
import org.chewing.v1.model.EmoticonPack
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "emoticon_pack", schema = "chewing")
class EmoticonPackJpaEntity(
    @Id
    @Column(name = "emoticon_pack_id")
    val emoticonPackId: String = UUID.randomUUID().toString(),

    @Column(name = "emoticon_pack_url", nullable = false)
    val emoticonPackUrl: String,

    @Column(name = "emoticon_pack_name", nullable = false)
    private val emoticonPackName: String,

    @JoinColumn(name = "emoticon_pack_id")
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val emoticons: List<EmoticonJpaEntity> = mutableListOf()
) : BaseEntity() {

    companion object {
        fun fromEmoticonPack(emoticonPack: EmoticonPack): EmoticonPackJpaEntity {
            return EmoticonPackJpaEntity(
                emoticonPack.emoticonPackId,
                emoticonPack.emoticonPackUrl,
                emoticonPack.emoticonPackName
            )
        }
    }

    fun toEmoticonPack(): EmoticonPack {
        return EmoticonPack.of(
            emoticonPackId,
            emoticonPackName,
            emoticonPackUrl,
            emoticons.map { it.toEmoticon() }
        )
    }
}