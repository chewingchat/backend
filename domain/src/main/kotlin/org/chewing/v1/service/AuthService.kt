package org.chewing.v1.service

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.UnauthorizedException
import org.chewing.v1.implementation.*
import org.chewing.v1.model.*
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.repository.AuthRepository
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val authChecker: AuthChecker,
    private val pushTokenProcessor: PushTokenProcessor,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val authReader: AuthReader,
    private val authAppender: AuthAppender,
    private val userAppender: UserAppender,
    private val authRepository: AuthRepository
    // tokenProvider: JwtTokenProvider --> 이건 뭔 뜻이죠?
) {



    fun sendPhoneVerification(phoneNumber: String, countryCode: String) {
        // authChecker.checkPhoneNumberRegistered(phoneNumber) // --> 필요없어보여서 뻄
        // 휴대폰 인증번호 전송하기 전에 db에 인증 정보 저장하는 로직 구현
        val phone = Phone.generate(countryCode, phoneNumber) // 먼저 Phone 인스턴스 생성
        val phoneWithCode = phone.generateValidationCode() // 인증번호 생성

        authAppender.appendPhoneVerificationInfo(phoneWithCode) // authinfo에 인증번호 저장


        // AWS써서 phoneWithCode = phone.generateValidationCode()에서 생성한 인증번호 전송 로직 필요



    }

    fun sendEmailVerification(emailAddress: String) {
        // authChecker.checkEmailRegistered("", emailAddress) --> 필요없어보여서 뺌
        // 이메일 인증번호 전송하기 전에 db에 인증 정보 저장하는 로직 구현
        val email = Email.generate(emailAddress)
        val emailWithCode = email.generateValidationCode()


        authAppender.appendEmailVerificationInfo(emailWithCode)

        // AWS써서 인증번호 전송

        // 실제 구현: 이메일 전송 API를 사용하여 인증번호 전송

    }

    //트랜잭션 처리 필요없는 부분
    // processor에서 트랜잭션 걸게욥
    fun verifyPhoneAndLogin(
        phone: Phone,
        pushToken: PushToken,
    ): Pair<String, String> {

        // 인증번호 보여주는 것과 사용자가 인증코드 입력하는 건 프론트에서 처리

        // 사용자가 인증코드 입력 후 휴대폰 인증 번호 확인용 읽기(인증번호 받음)
        val savedPhone = authReader.readPhoneNumber(phone.number, phone.country)



        // 받은 휴대폰 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생
        AuthValidator.validatePhoneNumber(savedPhone, phone.validationCode.code)

        //유저 읽기
        val (user, authInfo) = authReader.readInfoWithUserByPhoneNumber(phone.number)

        //푸시 토큰 처리
        pushTokenProcessor.processPushToken(user, pushToken)


        val accessToken = jwtTokenProvider.createToken(user.userId.value())
        val refreshToken = jwtTokenProvider.createRefreshToken(user.userId.value())

        //로그인 정보 저장 -> refresh Token 저장 해야함
        authAppender.appendLoggedInInfo(authInfo, refreshToken)
        return Pair(accessToken, refreshToken.token)
    }

    //밑에 verifyEmailAndLogin, verifyEmailAndSignup, verifyPhoneAndSignup, 수정

    fun verifyEmailAndLogin(
        email: Email,
        pushToken: PushToken,
    ): Pair<String, String> {

        // 인증번호 보여주는 것과 사용자가 인증코드 입력하는 건 프론트에서 처리

        // 사용자가 인증코드 입력 후 이메일 인증 번호 확인용 읽기(인증번호 받음)
        val savedEmail = authReader.readEmail(email.emailAddress)



        // 받은 이메일 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생
        AuthValidator.validateEmail(savedEmail, email.validationCode.code)

        //유저 읽기
        val (user, authInfo) = authReader.readInfoWithUserByEmail(email.emailId)


        //푸시 토큰 처리
        pushTokenProcessor.processPushToken(user, pushToken)


        val accessToken = jwtTokenProvider.createToken(user.userId.value())
        val refreshToken = jwtTokenProvider.createRefreshToken(user.userId.value())

        //로그인 정보 저장 -> refresh Token 저장 해야함
        authAppender.appendLoggedInInfo(authInfo, refreshToken)
        return Pair(accessToken, refreshToken.token)
    }


    fun verifyEmailAndSignup(
        email: Email,
        pushToken: PushToken,
    ): Pair<String, String> {

        // 인증번호 보여주는 것과 사용자가 인증코드 입력하는 건 프론트에서 처리

        // 사용자가 인증코드 입력 후 이메일 인증 번호 확인용 읽기(인증번호 받음)
        val savedEmail = authReader.readEmail(email.emailId)


        // 이미 인증된 사용자인지 확인
        AuthValidator.EmailvalidateIsAuthorizedFirst(savedEmail)

        // 받은 이메일 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생
        AuthValidator.validateEmail(savedEmail, email.validationCode.code)



        // 인증 정보를 불러오기 (sendEmailVerification에서 저장된 정보 사용)
        val authInfo = authReader.readInfoWithUserByEmail(email.emailAddress).second


        // 새로운 유저 정보 받아서 저장 (UserJpaEntity로 저장) ---> 수정해야함(프론트엔드)
        val newUser = User.generate(
            birth = "1990-01-01", // 예시로 추가한 기본 정보. 필요에 맞게 수정해야 함
            firstName = "FirstName",
            lastName = "LastName"
        )

        // UserJpaEntity에 저장 (UserAppender 사용)
        userAppender.appendUser(newUser)




        //푸시 토큰 처리
        pushTokenProcessor.processPushToken(newUser, pushToken)


        val accessToken = jwtTokenProvider.createToken(newUser.userId.value())
        val refreshToken = jwtTokenProvider.createRefreshToken(newUser.userId.value())





        //로그인 정보 저장 -> refresh Token 저장 해야함
        authAppender.appendLoggedInInfo(authInfo, refreshToken)
        return Pair(accessToken, refreshToken.token)

    }


    fun verifyPhoneAndSignup(
        phone: Phone,
        pushToken: PushToken,
    ): Pair<String, String> {

        // 인증번호 보여주는 것과 사용자가 인증코드 입력하는 건 프론트에서 처리

        // 사용자가 인증코드 입력 후 휴대폰 인증 번호 확인용 읽기(인증번호 받음)
        val savedPhone = authReader.readPhoneNumber(phone.number, phone.country)



        // 이미 인증된 사용자인지 확인
        AuthValidator.PhonevalidateIsAuthorizedFirst(savedPhone)

        // 받은 휴대폰 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생
        AuthValidator.validatePhoneNumber(savedPhone, phone.validationCode.code)


        // 휴대폰 인증 정보를 불러오기 (sendPhoneVerification에서 저장된 정보 사용)
        val authInfo = authReader.readInfoWithUserByPhoneNumber(phone.number).second

        // 새로운 유저 정보 받아서 저장 (UserJpaEntity로 저장) ---> 수정해야함(프론트엔드)
        val newUser = User.generate(
            birth = "1990-01-01", // 예시로 추가한 기본 정보. 필요에 맞게 수정해야 함
            firstName = "FirstName",
            lastName = "LastName"
        )

        // UserJpaEntity에 저장 (UserAppender 사용)
        userAppender.appendUser(newUser)

        //푸시 토큰 처리
        pushTokenProcessor.processPushToken(newUser, pushToken)


        val accessToken = jwtTokenProvider.createToken(newUser.userId.value())
        val refreshToken = jwtTokenProvider.createRefreshToken(newUser.userId.value())




        //로그인 정보 저장 -> refresh Token 저장 해야함
        authAppender.appendLoggedInInfo(authInfo, refreshToken)
        return Pair(accessToken, refreshToken.token)

    }



    fun logout(accessToken: String) {

        // AccessToken에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)

        // 사용자 정보 확인 (예외 처리 포함)
        val userEntity = userRepository.readUserById(User.UserId.of(userId))
            ?: throw IllegalArgumentException("Invalid access token")

        // 프론트엔드에서 access 토큰 삭제 명령 및 삭제

        // 사용자 로그인 정보를 삭제 (refresh token 정보 포함)
        authRepository.deleteLoggedInInfo(userEntity.userId)

        // 로그아웃 완료 메시지
        println("User ${userEntity.userId.value()} has been logged out successfully.")



    }

    fun refreshAccessToken(refreshToken: String): Pair<String, String> {
        // "Bearer " 제거
        val token = refreshToken.removePrefix("Bearer ")

        // 리프레시 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            throw UnauthorizedException(ErrorCode.AUTH_5)
        }

        // 리프레시 토큰에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        // 새 액세스 토큰 및 리프레시 토큰 생성
        val newAccessToken = jwtTokenProvider.createToken(userId)
        val newRefreshToken = jwtTokenProvider.createRefreshToken(userId).token

        // Pair로 반환
        return Pair(newAccessToken, newRefreshToken)
    }



}