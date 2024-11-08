package org.chewing.v1.model.schedule

import java.time.Month
import java.time.Year

class ScheduleType private constructor(
     val year: Int,
     val month: Int,
) {
    companion object {
        fun of(
            year: Int,
            month: Int,
        ): ScheduleType {
            return ScheduleType(year, month)
        }
    }
}