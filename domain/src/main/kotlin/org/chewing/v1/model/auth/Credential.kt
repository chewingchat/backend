package org.chewing.v1.model.auth

import org.chewing.v1.model.contact.ContactType

interface Credential {
    val type: ContactType
}