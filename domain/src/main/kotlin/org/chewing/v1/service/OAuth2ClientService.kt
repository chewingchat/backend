package org.chewing.v1.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OAuth2ClientService(
    private val restTemplate: RestTemplate
) {
    //
    // 일단 가져오는 부분만 구현했고, 가져오기 전에 인증이랑 예외처리부분은 홈페이지에서 인증하는 건지 스프링에서 인증하는 것인지
    // 헷갈려서 일단 제외했습니다.


    // 구글 사용자 정보 가져오기
    fun getGoogleUserInfo(oauthToken: String): GoogleUserInfo {
        val url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=$oauthToken"
        return restTemplate.getForObject(url, GoogleUserInfo::class.java)!!
    }

    // 애플 사용자 정보 가져오기
    fun getAppleUserInfo(oauthToken: String): AppleUserInfo {
        val url = "https://appleid.apple.com/auth/token"
        return restTemplate.getForObject(url, AppleUserInfo::class.java)!!
    }
}

data class GoogleUserInfo(
    val id: String,
    val email: String,
    val name: String,
    val birth: String
)

data class AppleUserInfo(
    val id: String,
    val email: String,
    val name: String,
    val birth: String
)