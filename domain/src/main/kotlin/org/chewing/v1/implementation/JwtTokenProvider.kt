package org.chewing.v1.implementation

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.UnauthorizedException
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.token.RefreshToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKeyString: String,
    @Value("\${jwt.access-expiration}") private val accessExpiration: Long,
    @Value("\${jwt.refresh-expiration}") private val refreshExpiration: Long,
) {
    private val secretKey: SecretKey
        get() = Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    fun createJwtToken(userId: String): JwtToken {
        val accessToken = createAccessToken(userId)
        val refreshToken = createRefreshToken(userId)
        return JwtToken.of(accessToken, refreshToken)
    }
    // JWT Access Token 생성
    fun createAccessToken(userId: String): String {
        val claims: Claims = Jwts.claims().setSubject(userId)
        val now = Date()
        val expiryDate = Date(now.time + accessExpiration)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
    }

    // JWT Refresh Token 생성
    fun createRefreshToken(userId: String): RefreshToken {
        val claims: Claims = Jwts.claims().setSubject(userId)
        val now = Date()
        val expiryDate = Date(now.time + refreshExpiration)
        val token = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
        return RefreshToken.of(token, LocalDateTime.from(expiryDate.toInstant()))
    }

    // 토큰 유효성 검사 수정함(예외 발생 방식으로 수정)
    fun validateToken(token: String) {
        try {
            val claims = getClaimsFromToken(token)
            if (claims.expiration.before(Date())) {
                throw UnauthorizedException(ErrorCode.ACCESS_TOKEN_EXPIRED)  // 엑세스 토큰 만료 예외 발생
            }
        } catch (e: ExpiredJwtException) {
            throw UnauthorizedException(ErrorCode.ACCESS_TOKEN_EXPIRED)  // 엑세스 토큰 만료 예외 발생
        } catch (e: JwtException) {
            throw UnauthorizedException(ErrorCode.REFRESH_TOKEN_EXPIRED)  // 리프레시 토큰 만료 예외 발생
        } catch (e: Exception) {
            throw UnauthorizedException(ErrorCode.VALIDATE_EXPIRED)  // 기타 예외 발생
        }
    }

    // 리프레시 토큰 유효성 검사 추가
    fun validateRefreshToken(refreshToken: String) {
        validateToken(refreshToken)  // 동일한 유효성 검사 메서드 사용
    }




    // 토큰에서 사용자 ID 추출
    fun getUserIdFromToken(token: String): String {
        val claims = getClaimsFromToken(token)
        return claims.subject
    }

    // 토큰에서 클레임 추출
    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }




}
