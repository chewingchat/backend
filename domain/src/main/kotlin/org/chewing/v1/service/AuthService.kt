package org.chewing.v1.service

import org.chewing.v1.implementation.*
import org.chewing.v1.implementation.auth.*
import org.chewing.v1.implementation.user.UserAppender
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.*
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.contact.PhoneNumber

import org.springframework.stereotype.Service


@Service
class AuthService(
    private val authChecker: AuthChecker,
    private val pushTokenProcessor: PushTokenProcessor,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authReader: AuthReader,
    private val authAppender: AuthAppender,
    private val authRemover: AuthRemover,
    private val userReader: UserReader,
    private val userAppender: UserAppender,
    private val authUpdater: AuthUpdater,
    // tokenProvider: JwtTokenProvider --> 이건 뭔 뜻이죠?
) {


    fun sendPhoneVerification(phoneNumber: PhoneNumber) {
        // authChecker.checkPhoneNumberRegistered(phoneNumber) // --> 필요없어보여서 뻄
        // 휴대폰 인증번호 전송하기 전에 db에 인증 정보 저장하는 로직 구현
        // 변수에 NULL처리 때문에 객체 생성하는거 막을게요 ㅠㅠㅠ.. 저도 코드 다 바꾸는중 ㅠ
        authAppender.appendPhoneVerification(phoneNumber) // authinfo에 인증번호 저장


        // AWS써서 phoneWithCode = phone.generateValidationCode()에서 생성한 인증번호 전송 로직 필요


    }

    fun sendEmailVerification(emailAddress: String) {
        // authChecker.checkEmailRegistered("", emailAddress) --> 필요없어보여서 뺌
        // 이메일 인증번호 전송하기 전에 db에 인증 정보 저장하는 로직 구현
        authAppender.appendEmailVerification(emailAddress)

        // AWS써서 인증번호 전송

        // 실제 구현: 이메일 전송 API를 사용하여 인증번호 전송

    }

    //트랜잭션 처리 필요없는 부분
    // processor에서 트랜잭션 걸게욥
    fun verifyPhone(
        phoneNumber: PhoneNumber,
        verificationCode: String,
        appToken: String,
        device: PushToken.Device
    ): Pair<JwtToken, User> {
        // 인증번호 보여주는 것과 사용자가 인증코드 입력하는 건 프론트에서 처리

        // 사용자가 인증코드 입력 후 휴대폰 인증 번호 확인용 읽기(인증번호 받음) -> 조
        val savedPhone = authReader.readPhoneNumber(phoneNumber)
        // 받은 휴대폰 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생 -> 좋아욥
        AuthValidator.validatePhoneNumber(savedPhone, verificationCode)
        //유저 읽기
        val user = userAppender.appendIfNotExsist(savedPhone)
        //푸시 토큰 처리
        pushTokenProcessor.processPushToken(user, appToken, device)

        // 로그인 처리
        val token = jwtTokenProvider.createJwtToken(user.userId)

        authAppender.appendLoggedInInfo(token.refreshToken, user)

        return Pair(token, user)
    }

    //밑에 verifyEmailAndLogin, verifyEmailAndSignup, verifyPhoneAndSignup, 수정
    fun verifyEmail(
        emailAddress: String,
        verificationCode: String,
        appToken: String,
        device: PushToken.Device
    ): Pair<JwtToken, User> {

        // 인증번호 보여주는 것과 사용자가 인증코드 입력하는 건 프론트에서 처리

        // 사용자가 인증코드 입력 후 이메일 인증 번호 확인용 읽기(인증번호 받음)
        val savedEmail = authReader.readEmail(emailAddress)
        // 받은 이메일 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생
        AuthValidator.validateEmail(savedEmail, verificationCode)

        val user = userAppender.appendIfNotExsist(savedEmail)
        //푸시 토큰 처리
        pushTokenProcessor.processPushToken(user, appToken, device)
        // 로그인 처리
        val token = jwtTokenProvider.createJwtToken(user.userId)

        authAppender.appendLoggedInInfo(token.refreshToken, user)
        return Pair(token, user)
    }

    fun logout(accessToken: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)
        // 사용자 로그인 정보 삭제 (리프레시 토큰 포함)
        authRemover.removeLoginInfo(userId)
    }

    fun refreshAccessToken(refreshToken: String): JwtToken {
        // "Bearer " 제거
        val token = refreshToken.removePrefix("Bearer ")

        // 리프레시 토큰 유효성 검사(수정)
        jwtTokenProvider.validateRefreshToken(refreshToken)

        // 리프레시 토큰에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        val user = userReader.read(userId)

        val newToken = jwtTokenProvider.createJwtToken(user.userId)

        authAppender.appendLoggedInInfo(newToken.refreshToken, user)

        return newToken
    }

    // 수정 로직 추가(더 추가)
    // 전화번호 수정 전 인증 요청 로직
    fun sendPhoneVerificationForUpdate(userId: String, phoneNumber: PhoneNumber) {
        // 번호 중복 체크 로직 (이미 사용 중인 번호인지 확인)
        authChecker.checkPhoneNumberIsUsed(phoneNumber, userId)
        authUpdater.updatePhoneVerificationCode(phoneNumber)
    }

    // 전화번호 인증 및 수정 로직

    fun verifyPhoneForUpdate(userId: String, phoneNumber: PhoneNumber, verificationCode: String) {
        // 인증 정보 읽기
        val savedPhone = authReader.readPhoneNumber(phoneNumber)

        // 인증번호 검증
        AuthValidator.validatePhoneNumber(savedPhone, verificationCode)

        // 업데이트 로직 실행
        authUpdater.updatePhoneNumber(phoneNumber)
    }

    // 이메일 수정 전 인증 요청 로직
    fun sendEmailVerificationForUpdate(userId: String, emailAddress: String) {
        // 이메일 중복 체크 로직 (이미 사용 중인 이메일인지 확인)
        authChecker.checkEmailIsUsed(emailAddress, userId)

        authUpdater.updateEmailVerificationCode(emailAddress)
    }

    // 이메일 인증 및 수정 로직
    fun verifyEmailForUpdate(userId: String, email: String, verificationCode: String) {
        // 인증 정보 읽기
        val savedEmail = authReader.readEmail(email)

        // 인증번호 검증
        AuthValidator.validateEmail(savedEmail, verificationCode)

        // 업데이트 로직 실행
        authUpdater.updateEmail(email)
    }

}