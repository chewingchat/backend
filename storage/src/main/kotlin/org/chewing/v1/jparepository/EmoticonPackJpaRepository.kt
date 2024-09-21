package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.emoticon.EmoticonJpaEntity
import org.chewing.v1.jpaentity.emoticon.EmoticonPackJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

internal interface EmoticonPackJpaRepository : JpaRepository<EmoticonPackJpaEntity, String> {
}