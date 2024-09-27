package org.chewing.v1.implementation.user

import org.chewing.v1.implementation.emoticon.EmoticonReader
import org.chewing.v1.model.emoticon.EmoticonPack
import org.springframework.stereotype.Component

@Component
class UserEmoticonFinder(
    private val userReader: UserReader,
    private val emoticonReader: EmoticonReader
) {
    fun find(userId: String): List<EmoticonPack> {
        val emoticonPackIds = userReader.readOwnEmoticonPacks(userId)
        val emoticon = emoticonReader.readEmoticonPacks(emoticonPackIds)
        return emoticon
    }
}