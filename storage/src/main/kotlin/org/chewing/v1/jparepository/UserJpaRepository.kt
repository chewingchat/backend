package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
interface UserJpaRepository: JpaRepository<UserJpaEntity, String> {
}