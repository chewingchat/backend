package org.chewing.v1.implementation

import io.jsonwebtoken.*
import org.chewing.v1.model.token.RefreshToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.access-expiration}") private val accessExpiration: Long,
    @Value("\${jwt.refresh-expiration}") private val refreshExpiration: Long
) {

    // JWT Access Token 생성
    fun createToken(userId: String): String {
        val claims: Claims = Jwts.claims().setSubject(userId)
        val now = Date()
        val expiryDate = Date(now.time + accessExpiration)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    // JWT Refresh Token 생성
    fun createRefreshToken(userId: String): RefreshToken {
        val claims: Claims = Jwts.claims().setSubject(userId)
        val now = Date()
        val expiryDate = Date(now.time + refreshExpiration)
        val token =  Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
        return RefreshToken.of(token, LocalDateTime.from(expiryDate.toInstant()))
    }

    // 토큰 유효성 검사
    fun validateToken(token: String): Boolean {
        try {
            val claims = getClaimsFromToken(token)
            return !claims.expiration.before(Date())
        } catch (e: Exception) {
            return false
        }
    }

    // 토큰에서 사용자 ID 추출
    fun getUserIdFromToken(token: String): String {
        val claims = getClaimsFromToken(token)
        return claims.subject
    }

    // 토큰에서 클레임 추출
    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
    }
}
