package org.chewing.v1.implementation.media

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.chewing.v1.TestDataFactory
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.external.ExternalFileClient
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.util.AsyncJobExecutor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class FileHandlerTest {
    private val externalFileClient: ExternalFileClient = mock()
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val asyncJobExecutor = AsyncJobExecutor(ioScope)

    private val fileAppender = FileAppender(externalFileClient)
    private val fileRemover = FileRemover(externalFileClient)
    private val fileGenerator = FileGenerator()
    private val fileValidator = FileValidator()

    private val fileHandler = FileHandler(fileAppender, fileRemover, fileGenerator, fileValidator, asyncJobExecutor)

    @Test
    fun `파일 목록 생성 테스트 - 실패(파일 이름이 올바르지 않음)`(){
        val userId = "userId"
        val files = listOf(
            TestDataFactory.createFileData(MediaType.IMAGE_PNG,"0.png"),
            TestDataFactory.createFileData(MediaType.VIDEO_MP4,"4.mp4"),
        )
        val exception = assertThrows<ConflictException> {
            fileHandler.handleNewFiles(userId, files, FileCategory.FEED)
        }
        assert(exception.errorCode == ErrorCode.FILE_NAME_INCORRECT)
    }

    @Test
    fun `파일 목록 전송시 하나라도 실패한다면 Exception을 던져야함`(){
        val userId = "userId"
        val files = listOf(
            TestDataFactory.createFileData(MediaType.IMAGE_PNG,"0.png"),
            TestDataFactory.createFileData(MediaType.IMAGE_PNG,"1.png"),
        )

        whenever(externalFileClient.uploadFile(eq(files[0]), any()))
            .thenThrow(RuntimeException())

        val exception = assertThrows<ConflictException> {
            fileHandler.handleNewFiles(userId, files, FileCategory.FEED)
        }

        assert(exception.errorCode == ErrorCode.FILE_UPLOAD_FAILED)
    }

    @Test
    fun `파일목록 생성 테스트 - 성공`(){
        val userId = "userId"
        val files = listOf(
            TestDataFactory.createFileData(MediaType.IMAGE_PNG,"0.png"),
            TestDataFactory.createFileData(MediaType.IMAGE_PNG,"1.png"),
        )

        assertDoesNotThrow {
            fileHandler.handleNewFiles(userId, files, FileCategory.FEED)
        }
    }

    @Test
    fun `파일 생성 테스트 - 실패`(){
        val userId = "userId"
        val file = TestDataFactory.createFileData(MediaType.IMAGE_PNG,"1.png")

        val exception = assertThrows<ConflictException> {
            fileHandler.handleNewFile(userId, file, FileCategory.FEED)
        }
        assert(exception.errorCode == ErrorCode.FILE_NAME_INCORRECT)
    }

    @Test
    fun `파일 전송 실패시 Exception을 던져야함`(){
        val userId = "userId"
        val file = TestDataFactory.createFileData(MediaType.IMAGE_PNG,"0.png")

        whenever(externalFileClient.uploadFile(eq(file), any()))
            .thenThrow(RuntimeException())

        val exception = assertThrows<ConflictException> {
            fileHandler.handleNewFile(userId, file, FileCategory.FEED)
        }

        assert(exception.errorCode == ErrorCode.FILE_UPLOAD_FAILED)
    }

    @Test
    fun `파일 생성 테스트 - 성공`(){
        val userId = "userId"
        val file = TestDataFactory.createFileData(MediaType.IMAGE_PNG,"0.png")

        assertDoesNotThrow {
            fileHandler.handleNewFile(userId, file, FileCategory.FEED)
        }
    }

    @Test
    fun `파일 삭제 테스트 - 성공`(){
        val media = TestDataFactory.createMedia(FileCategory.PROFILE, 0, MediaType.IMAGE_PNG)

        assertDoesNotThrow {
            fileHandler.handleOldFile(media)
        }
    }

    @Test
    fun `파일 목록 삭제 테스트 - 실패`(){
        val media1 = TestDataFactory.createMedia(FileCategory.FEED,0,MediaType.IMAGE_PNG)
        val media2 = TestDataFactory.createMedia(FileCategory.FEED,0,MediaType.IMAGE_PNG)

        whenever(externalFileClient.removeFile(media2)).thenThrow(RuntimeException())

        val exception = assertThrows<ConflictException> {
            fileHandler.handleOldFiles(listOf(media1,media2))
        }

        assert(exception.errorCode == ErrorCode.FILE_DELETE_FAILED)
    }

    @Test
    fun `파일 목록 삭제 테스트 성공`(){
        val media1 = TestDataFactory.createMedia(FileCategory.FEED,0,MediaType.IMAGE_PNG)
        val media2 = TestDataFactory.createMedia(FileCategory.FEED,0,MediaType.IMAGE_PNG)

        assertDoesNotThrow {
            fileHandler.handleOldFiles(listOf(media1,media2))
        }
    }

    @Test
    fun `기본 파일 이라면 삭제를 하지 않음`(){
        val media = TestDataFactory.createMedia(FileCategory.PROFILE, 0, MediaType.IMAGE_BASIC)
        assertDoesNotThrow {
            fileHandler.handleOldFile(media)
        }
        verify(externalFileClient, never()).removeFile(any())
    }
}