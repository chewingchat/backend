package org.chewing.v1.model

class Emoticon private constructor(
    val emoticonId: String,
    val emoticonName: String,
    val emoticonUrl: String,
) {
    companion object {
        fun empty(): Emoticon {
            return Emoticon(
                emoticonId = "",
                emoticonName = "",
                emoticonUrl = "",
            )
        }
        fun of(
            emoticonId: String,
            emoticonName: String,
            emoticonUrl: String,
        ): Emoticon {
            return Emoticon(
                emoticonId = emoticonId,
                emoticonName = emoticonName,
                emoticonUrl = emoticonUrl,
            )
        }
    }
}