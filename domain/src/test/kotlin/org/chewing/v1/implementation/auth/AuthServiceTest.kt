//package org.chewing.v1.implementation.auth
//
//import org.chewing.v1.external.ExternalEmailClient
//import org.chewing.v1.external.ExternalPhoneClient
//import org.chewing.v1.implementation.schedule.ScheduleRemover
//import org.chewing.v1.implementation.user.*
//import org.chewing.v1.repository.AuthRepository
//import org.chewing.v1.repository.UserRepository
//import org.chewing.v1.repository.UserStatusRepository
//import org.chewing.v1.service.AuthService
//import org.mockito.kotlin.mock
//import org.springframework.stereotype.Component
//
//class AuthServiceTest {
//    private val authRepository: AuthRepository = mock()
//    private val userRepository: UserRepository = mock()
//    private val externalPhoneClient: ExternalPhoneClient = mock()
//    private val externalEmailClient: ExternalEmailClient = mock()
//    private val userProcessor: UserProcessor = mock()
//    private val authProcessor: AuthProcessor = mock()
//    private val userChecker: UserChecker = UserChecker(userRepository)
//    private val authChecker: AuthChecker = AuthChecker(authRepository, userChecker)
//    private val jwtTokenProvider: JwtTokenProvider = mock()
//    private val authReader: AuthReader = AuthReader(authRepository)
//    private val authAppender: AuthAppender = AuthAppender(authRepository)
//    private val authRemover: AuthRemover = AuthRemover(authRepository)
//    private val userReader: UserReader = UserReader(userRepository)
//    private val authUpdater: AuthUpdater = AuthUpdater(authRepository)
//    private val authSender: AuthSender = AuthSender(externalPhoneClient, externalEmailClient)
//    private val authValidator: AuthValidator = AuthValidator()
//    private val authService: AuthService = AuthService(
//        authChecker,
//        userProcessor,
//        jwtTokenProvider,
//        authReader,
//        authAppender,
//        authRemover,
//        userReader,
//        authUpdater,
//        authSender,
//        authValidator,
//        authProcessor
//    )
//}