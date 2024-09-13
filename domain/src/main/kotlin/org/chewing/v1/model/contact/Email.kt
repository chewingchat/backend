package org.chewing.v1.model.contact


class Email private constructor(
    val email: String
) : Contact {
    companion object {
        fun of(email: String): Email {
            return Email(
                email = email
            )
        }
    }

    override val type: ContactType
        get() = ContactType.EMAIL
}
