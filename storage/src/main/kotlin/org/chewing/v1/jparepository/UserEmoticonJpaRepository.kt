package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.emoticon.EmoticonPackJpaEntity
import org.chewing.v1.jpaentity.user.UserEmoticonJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface UserEmoticonJpaRepository : JpaRepository<UserEmoticonJpaEntity, String> {
}