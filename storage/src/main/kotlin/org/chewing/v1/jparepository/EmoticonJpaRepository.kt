package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.emoticon.EmoticonJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface EmoticonJpaRepository : JpaRepository<EmoticonJpaEntity, String> {}