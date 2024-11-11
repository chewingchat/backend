package org.chewing.v1.external

import org.chewing.v1.config.IntegrationTest
import org.chewing.v1.implementation.media.FileHandler
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.util.FileUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.imageio.ImageIO

@ActiveProfiles("local")
class ExternalSendTest : IntegrationTest() {
    @Autowired
    private lateinit var fileHandler: FileHandler

    @Test
    fun test() {
        val imageWidth = 100
        val imageHeight = 100
        val bufferedImage = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)

        // 이미지에 그래픽 그리기
        val graphics = bufferedImage.createGraphics()
        graphics.color = Color.BLUE
        graphics.fillRect(0, 0, imageWidth, imageHeight)
        graphics.color = Color.WHITE
        graphics.drawString("Hello, World!", 10, 50)
        graphics.dispose()

        // 이미지를 바이트 배열로 변환
        val baos = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "jpg", baos)
        val imageBytes = baos.toByteArray()

        // MockMultipartFile 생성
        val file = MockMultipartFile(
            "file",
            "0.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageBytes,
        )
        val fileData = FileUtil.convertMultipartFileToFileData(file)
        fileHandler.handleNewFile("testUserId", fileData, FileCategory.PROFILE)
        val localFilePath = "C:/temp/${file.originalFilename}" // 원하는 경로로 변경하세요

        // 로컬 파일로 저장하는 함수 호출
        saveMultipartFileToLocal(file, localFilePath)
    }
    private fun saveMultipartFileToLocal(multipartFile: MockMultipartFile, filePath: String) {
        try {
            val file = File(filePath)
            // 부모 디렉토리가 없으면 생성
            file.parentFile.mkdirs()
            // 파일 출력 스트림을 사용하여 파일 저장
            FileOutputStream(file).use { fos ->
                fos.write(multipartFile.bytes)
            }
            println("파일이 성공적으로 저장되었습니다: $filePath")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
