package org.chewing.v1.facade

import org.chewing.v1.TestDataFactory
import org.chewing.v1.service.auth.AuthService
import org.chewing.v1.service.friend.FriendShipService
import org.chewing.v1.service.user.UserService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FriendFacadeTest {
    private val friendShipService: FriendShipService = mock()
    private val userService: UserService = mock()
    private val authService: AuthService = mock()

    private val friendFacade = FriendFacade(friendShipService, userService, authService)

    @Test
    fun `친구 추가`() {
        // given
        val userId = "123"
        val friendName = TestDataFactory.createUserName()
        val verificationCode = "123"
        val targetContact = TestDataFactory.createPhoneNumber()
        val contact = TestDataFactory.createPhone(verificationCode)
        val targetUser = TestDataFactory.createUser(userId)
        val userAccount = TestDataFactory.createUserAccount(null, null)

        whenever(authService.getContact(targetContact)).thenReturn(contact)
        whenever(userService.getUserByContact(contact)).thenReturn(targetUser)
        whenever(userService.getUserAccount(userId)).thenReturn(userAccount)

        friendFacade.addFriend(userId, friendName, targetContact)

        verify(friendShipService).creatFriendShip(userId, userAccount.user.name, targetUser.userId, friendName)
    }
}
