package org.chewing.v1.service

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.error.UnauthorizedException
import org.chewing.v1.implementation.JwtTokenProvider
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.UserProcessor
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.user.UserUpdater
import org.chewing.v1.model.User
import org.chewing.v1.repository.AuthRepository
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Service

import java.io.File

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userProcessor: UserProcessor,
    // 추가
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userUpdater: UserUpdater,
) {
    fun updateUserImage(file: File, userId: User.UserId) {
        val media = fileProcessor.processNewFile(userId, file)
        val preMedia = userProcessor.processChangeUserImage(userId, media)
        fileProcessor.processPreFile(preMedia)
    }
    fun getUserInfo(userId: User.UserId): User {
        return userReader.readUserWithStatus(userId)
    }

    // 회원탈퇴로직(추가)
    fun deleteAccount(accessToken: String) {
        // accessToken을 이용하여 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)

        // 사용자 존재 여부 확인
        val user = userRepository.readUserById(User.UserId.of(userId))
            ?: throw UnauthorizedException(ErrorCode.AUTH_4)  // 토큰 만료 또는 유저 미존재 시 에러

        // 로그인 정보 삭제 (refreshToken 정보 삭제)
        authRepository.deleteLoggedInInfo(user.userId)

        // 유저 정보 삭제
        userRepository.remove(user.userId)
    }
    fun updateUserName(accessToken: String, firstName: String, lastName: String) {
        // 토큰에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)

        // 유저 정보 가져오기
        val user = userRepository.readUserById(User.UserId.of(userId))
            ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)

        // 유저 이름 변경
        val updatedUser = user.updateName(firstName, lastName)

        // 유저 정보 업데이트
        userUpdater.updateUser(updatedUser)
    }


}