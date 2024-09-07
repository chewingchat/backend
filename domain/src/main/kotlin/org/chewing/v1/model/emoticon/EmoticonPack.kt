package org.chewing.v1.model.emoticon

class EmoticonPack(
    val emoticonPackId: String,
    val emoticonPackName: String,
    val emoticonPackUrl: String,
    val emoticons: List<Emoticon>
) {
    companion object {
        fun of(
            emoticonPackId: String,
            emoticonPackName: String,
            emoticonPackUrl: String,
            emoticons: List<Emoticon>
        ): EmoticonPack {
            return EmoticonPack(
                emoticonPackId = emoticonPackId,
                emoticonPackName = emoticonPackName,
                emoticonPackUrl = emoticonPackUrl,
                emoticons = emoticons
            )
        }
    }
}