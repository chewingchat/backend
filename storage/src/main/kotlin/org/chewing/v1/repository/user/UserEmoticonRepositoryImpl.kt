package org.chewing.v1.repository.user

import org.chewing.v1.jparepository.user.UserEmoticonJpaRepository
import org.chewing.v1.model.user.UserEmoticonPackInfo
import org.springframework.stereotype.Repository

@Repository
internal class UserEmoticonRepositoryImpl(
    private val userEmoticonJpaRepository: UserEmoticonJpaRepository
) : UserEmoticonRepository {
    override fun readUserEmoticons(userId: String): List<UserEmoticonPackInfo> {
        return userEmoticonJpaRepository.findAllByIdUserId(userId).map { it.toUserEmoticon() }
    }
}