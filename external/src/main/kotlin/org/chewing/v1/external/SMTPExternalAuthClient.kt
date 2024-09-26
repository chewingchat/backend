package org.chewing.v1.external

import org.springframework.stereotype.Component

@Component
class SMTPExternalAuthClient : ExternalEmailClient {
    override fun sendEmail(emailAddress: String, verificationCode: String) {
        println("Sending email to $emailAddress")
    }
}