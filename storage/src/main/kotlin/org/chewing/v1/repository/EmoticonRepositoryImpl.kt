package org.chewing.v1.repository

import org.chewing.v1.jparepository.EmoticonJpaRepository
import org.chewing.v1.jparepository.EmoticonPackJpaRepository
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.emoticon.EmoticonPack
import org.springframework.stereotype.Repository

@Repository
internal class EmoticonRepositoryImpl(
    private val emoticonJpaRepository: EmoticonJpaRepository,
    private val emoticonPackJpaRepository: EmoticonPackJpaRepository
) : EmoticonRepository {
    override fun readEmoticonPacks(emoticonPackIds: List<String>): List<EmoticonPack> {
        val emoticonPacks = emoticonPackJpaRepository.findAllById(emoticonPackIds).associateBy { it.emoticonPackId }
        val emoticonGroup = emoticonJpaRepository.findAllByEmoticonPackIdIn(emoticonPackIds).groupBy {
            it.emoticonId
        }
        return emoticonPacks.map { (emoticonPackId, emoticonPackJpaEntity) ->
            val emoticons = emoticonGroup[emoticonPackId]?.map { it.toEmoticon() } ?: emptyList()
            emoticonPackJpaEntity.toEmoticonPack(emoticons)
        }.toList()
    }

    override fun readEmoticon(emoticonId: String): Emoticon? {
        return emoticonJpaRepository.findById(emoticonId).map { it.toEmoticon() }.orElse(null)
    }

    override fun readEmoticons(emoticonIds: List<String>): List<Emoticon> {
        return emoticonJpaRepository.findAllById(emoticonIds).map { it.toEmoticon() }
    }
}