package org.chewing.v1.implementation.emoticon

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.User
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.emoticon.EmoticonPack
import org.chewing.v1.repository.EmoticonRepository
import org.springframework.stereotype.Component

@Component
class EmoticonReader(
    private val emoticonRepository: EmoticonRepository,
) {
    fun readEmoticonPacks(emoticonPackId: List<String>): List<EmoticonPack> {
        return emoticonRepository.readEmoticonPacks(emoticonPackId)
    }

    fun readEmoticon(emoticonId: String): Emoticon {
        return emoticonRepository.readEmoticon(emoticonId)?: throw NotFoundException(ErrorCode.EMOTICON_NOT_FOUND)
    }

    fun readEmoticons(emoticonIds: List<String>): List<Emoticon> {
        return emoticonRepository.readEmoticons(emoticonIds)
    }
}