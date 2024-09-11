package org.chewing.v1.implementation.media

import org.springframework.stereotype.Component
import java.io.File

@Component
class FileValidator(
) {
    fun validateFilesNameCorrect(files: List<File>) {
        files.forEachIndexed { index, file ->
            val expectedFileName = "$index."
            if (!file.name.startsWith(expectedFileName)) {
                throw IllegalArgumentException()
            }
        }
    }
    fun validateFileNameCorrect(file: File) {
        if (!file.name.startsWith("0.")) {
            throw IllegalArgumentException()
        }
    }
}