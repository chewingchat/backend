package org.chewing.v1.model

import java.time.Month
import java.time.Year

class ScheduleType private constructor(
    private val year: Year,
    private val month: Month,
) {
    companion object {
        fun of(
            year: String,
            month: String
        ): ScheduleType {
            return ScheduleType(Year.of(year.toInt()), Month.of(month.toInt()))
        }
    }
}