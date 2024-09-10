package org.chewing.v1.model.contact

class Phone private constructor(
    val country: String,
    val number: String,
) : Contact {
    companion object {
        fun of(country: String, number: String): Phone {
            return Phone(
                country = country,
                number = number,
            )
        }
    }

    override val type: ContactType
        get() = ContactType.PHONE
}
