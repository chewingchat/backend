package org.chewing.v1.service.tts

import org.chewing.v1.external.ExternalTtsClient
import org.springframework.stereotype.Service
import java.io.File

@Service
class TtsService(
    private val externalTtsClient: ExternalTtsClient
) {
    fun generateTtsFile(text: String, speaker: String): File {
        return externalTtsClient.generateTts(text, speaker)
    }
}
