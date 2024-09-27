package org.chewing.v1.external

interface ExternalEmailClient {
    fun sendEmail(emailAddress: String, verificationCode: String)
}