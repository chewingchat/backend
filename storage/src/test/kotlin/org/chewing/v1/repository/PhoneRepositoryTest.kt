package org.chewing.v1.repository

import org.chewing.v1.repository.support.TestDataGenerator
import org.chewing.v1.config.DbContextTest
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jparepository.PhoneJpaRepository
import org.chewing.v1.repository.support.PhoneProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class PhoneRepositoryTest : DbContextTest() {
    @Autowired
    private lateinit var phoneJpaRepository: PhoneJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

    private val phoneRepositoryImpl: PhoneRepositoryImpl by lazy {
        PhoneRepositoryImpl(phoneJpaRepository)
    }

    @Test
    fun `전화번호 조회에 성공해야 한다`() {
        val phoneNumber = PhoneProvider.buildNormalPhoneNumber()
        testDataGenerator.phoneEntityData(phoneNumber)

        val result = phoneRepositoryImpl.read(phoneNumber)

        assert(result != null)
        assert(result!!.number == phoneNumber.number)
        assert(result.countryCode == phoneNumber.countryCode)
    }

    @Test
    fun `전화번호 조회에 실패해야 한다`() {
        val phoneNumber = PhoneProvider.buildNormalPhoneNumber()
        testDataGenerator.phoneEntityData(phoneNumber)

        val wrongPhoneNumber = PhoneProvider.buildWrongPhoneNumber()

        val result = phoneRepositoryImpl.read(wrongPhoneNumber)

        assert(result == null)
    }

    @Test
    fun `전화번호가 존재 한다면 저장하지 않는다`() {
        val phoneNumber = PhoneProvider.buildNormalPhoneNumber()
        val oldPhone = testDataGenerator.phoneEntityData(phoneNumber)

        phoneRepositoryImpl.appendIfNotExists(phoneNumber)
        val result = phoneJpaRepository.findByNumberAndCountryCode(phoneNumber.number, phoneNumber.number)

        assert(result.isPresent)
        assert(result.get().toPhone().phoneId == oldPhone.phoneId)
    }

    @Test
    fun `전화번호가 존재하지 않는다면 저장한다`() {
        val phoneNumber = PhoneProvider.buildNormalPhoneNumber()
        phoneRepositoryImpl.appendIfNotExists(phoneNumber)
        val result = phoneJpaRepository.findByNumberAndCountryCode(phoneNumber.number, phoneNumber.countryCode)
        assert(result.isPresent)
        assert(result.get().toPhone().number == phoneNumber.number)
        assert(result.get().toPhone().countryCode == phoneNumber.countryCode)
    }

    @Test
    fun `전화번호를 전화번호 아이디로 읽는다`() {
        val phoneNumber = PhoneProvider.buildNormalPhoneNumber()
        val phone = testDataGenerator.phoneEntityData(phoneNumber)

        val result = phoneRepositoryImpl.readById(phone.phoneId)

        assert(result != null)
        assert(result!!.number == phoneNumber.number)
        assert(result.countryCode == phoneNumber.countryCode)
    }
}