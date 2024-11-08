package org.chewing.v1.config

import org.chewing.v1.util.StringToChatRoomSortCriteriaConverter
import org.chewing.v1.util.StringToFileCategory
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToChatRoomSortCriteriaConverter())
        registry.addConverter(StringToFileCategory())
    }
}
// Spring MVC 애플리케이션에서 문자열을 자동으로 특정 객체나 열거형으로 변환할 수 있도록 커스텀 변환기를 설정
// @RequestParam, @ModelAttribute, @PathVariable에서 알아서 convert해준다.

// "name_asc" 문자열을 서비스 계층에서 이해할 수 있는 SortCriteria 열거형(enum)으로 변환
// "image" 문자열을 FileCategory 객체로 변환
