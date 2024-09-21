package org.chewing.v1.implementation.emoticon

import org.chewing.v1.model.User
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.emoticon.EmoticonPack
import org.chewing.v1.repository.EmoticonRepository
import org.springframework.stereotype.Component

@Component
class EmoticonReader(
    private val emoticonRepository: EmoticonRepository,
) {
    fun readEmoticon(emoticonId: String): Emoticon {
        return emoticonRepository.readEmoticon(emoticonId)
    }
    fun readEmoticons(emoticonIds: List<String>): List<Emoticon> {
        return emoticonRepository.readEmoticons(emoticonIds)
    }
}