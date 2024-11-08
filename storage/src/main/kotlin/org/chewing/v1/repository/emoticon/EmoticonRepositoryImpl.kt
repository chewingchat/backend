package org.chewing.v1.repository.emoticon

import org.chewing.v1.jparepository.emoticon.EmoticonJpaRepository
import org.chewing.v1.jparepository.emoticon.EmoticonPackJpaRepository
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.emoticon.EmoticonInfo
import org.chewing.v1.model.emoticon.EmoticonPack
import org.chewing.v1.model.emoticon.EmoticonPackInfo
import org.springframework.stereotype.Repository

@Repository
internal class EmoticonRepositoryImpl(
    private val emoticonJpaRepository: EmoticonJpaRepository,
    private val emoticonPackJpaRepository: EmoticonPackJpaRepository
) : EmoticonRepository {
    override fun readEmoticonPacks(emoticonPackIds: List<String>): List<EmoticonPackInfo> {
        return emoticonPackJpaRepository.findAllById(emoticonPackIds).map { it.toEmoticonPack() }
    }

    override fun readEmoticons(emoticonPackIds: List<String>): List<EmoticonInfo> {
        return emoticonJpaRepository.findAllByEmoticonPackIdIn(emoticonPackIds).map { it.toEmoticon() }
    }
}