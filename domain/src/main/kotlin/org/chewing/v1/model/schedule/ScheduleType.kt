package org.chewing.v1.model.schedule

import java.time.Month
import java.time.Year

class ScheduleType private constructor(
     val year: Year,
     val month: Month,
) {
    companion object {
        fun of(
            year: Year,
            month: Month,
        ): ScheduleType {
            return ScheduleType(year, month)
        }
    }
}