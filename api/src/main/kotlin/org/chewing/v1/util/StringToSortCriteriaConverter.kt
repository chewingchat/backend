package org.chewing.v1.util

import org.chewing.v1.model.SortCriteria
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToSortCriteriaConverter : Converter<String, SortCriteria> {
    override fun convert(source: String): SortCriteria? {
        return SortCriteria.valueOf(source.uppercase()) // 대문자로 변환
    }
}