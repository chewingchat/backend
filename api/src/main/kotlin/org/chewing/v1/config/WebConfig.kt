package org.chewing.v1.config

import org.chewing.v1.util.StringToFileCategory
import org.chewing.v1.util.StringToSortCriteriaConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToSortCriteriaConverter())
        registry.addConverter(StringToFileCategory())
    }
}