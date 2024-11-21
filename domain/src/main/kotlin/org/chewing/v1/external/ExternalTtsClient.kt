package org.chewing.v1.external

import java.io.File

interface ExternalTtsClient {
    fun generateTts(text: String, speaker: String): File
}
