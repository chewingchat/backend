package org.chewing.v1.controller.tts

import org.chewing.v1.service.tts.TtsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files

@RestController
class TtsController(
    private val ttsService: TtsService
) {
    @GetMapping("/api/tts")
    fun generateTts(
        @RequestParam text: String,
        @RequestParam(defaultValue = "nara") speaker: String
    ): ResponseEntity<ByteArray> {
        val file: File = ttsService.generateTtsFile(text, speaker)
        val fileContent = Files.readAllBytes(file.toPath())
        file.delete() // 임시 파일 삭제
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=${file.name}")
            .body(fileContent)
    }
}
