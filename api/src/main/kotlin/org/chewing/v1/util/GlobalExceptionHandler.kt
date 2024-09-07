package org.chewing.v1.util

import org.chewing.v1.response.ErrorResponse
import org.chewing.v1.response.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import mu.KotlinLogging
import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException


@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): ErrorResponseEntity {
        val errorCode = ErrorCode.VARIABLE_WRONG
        logger.info { "파라미터가 누락됨: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.BAD_REQUEST, ErrorResponse.from(errorCode))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ErrorResponseEntity {
        val errorCode = ErrorCode.PATH_WRONG
        logger.info { "지원하지 않는 HTTP 메소드: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.BAD_REQUEST, ErrorResponse.from(errorCode))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ErrorResponseEntity {
        val errorCode = ErrorCode.VARIABLE_WRONG
        logger.info { "잘못된 인자: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.BAD_REQUEST, ErrorResponse.from(errorCode))
    }

    @ExceptionHandler(AuthorizationException::class)
    protected fun handleAuthorizationException(e: AuthorizationException): ErrorResponseEntity {
        val errorCode = e.errorCode
        logger.info { "인증 예외 발생: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.UNAUTHORIZED, ErrorResponse.from(errorCode))
    }
    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ErrorResponseEntity {
        val errorCode = ErrorCode.VARIABLE_WRONG
        logger.info { "잘못된 메세지: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.BAD_REQUEST, ErrorResponse.from(errorCode))
    }

    @ExceptionHandler(NotFoundException::class)
    protected fun handleNotFoundException(e: NotFoundException): ErrorResponseEntity {
        val errorCode = e.errorCode
        logger.info { "리소스를 찾을 수 없음: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.NOT_FOUND, ErrorResponse.from(errorCode))
    }

    @ExceptionHandler(ConflictException::class)
    protected fun handleConflictException(e: ConflictException): ErrorResponseEntity {
        val errorCode = e.errorCode
        logger.info { "충돌 예외 발생: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.CONFLICT, ErrorResponse.from(errorCode))
    }
}
