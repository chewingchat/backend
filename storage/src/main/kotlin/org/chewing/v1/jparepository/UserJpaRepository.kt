package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserJpaRepository : JpaRepository<UserJpaEntity, String> {

    @Query("SELECT u FROM UserJpaEntity u JOIN FETCH u.statusEmoticon s WHERE u.id = :id")
    fun findByIdWithStatusEmoticon(
        @Param("id") id: String
    ): Optional<UserJpaEntity>
}