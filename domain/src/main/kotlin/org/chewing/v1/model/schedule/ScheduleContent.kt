package org.chewing.v1.model.schedule

class ScheduleContent private constructor(
    val title: String,
    val text: String
) {
    companion object {
        fun of(
            title: String,
            text: String
        ): ScheduleContent {
            return ScheduleContent(
                title,
                text
            )
        }
    }
}