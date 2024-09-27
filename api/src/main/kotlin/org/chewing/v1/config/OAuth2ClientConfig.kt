package org.chewing.v1.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler

@Configuration
class OAuth2ClientConfig {

    @Bean
    fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
        return SimpleUrlAuthenticationSuccessHandler("/login/success")
    }
}
