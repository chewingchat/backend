package org.chewing.v1.controller

import org.chewing.v1.dto.response.schedule.ScheduleListResponse
import org.chewing.v1.model.ScheduleType
import org.chewing.v1.model.User
import org.chewing.v1.service.ScheduleService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/schedule")
class ScheduleController(
    private val scheduleService: ScheduleService
) {
    @GetMapping("")
    fun getSchedule(
        @RequestHeader("userId") userId: String,
        @RequestParam("year") year: String,
        @RequestParam("month") month: String
    ): SuccessResponseEntity<ScheduleListResponse> {
        val type = ScheduleType.of(year, month)
        val schedules = scheduleService.getSchedules(User.UserId.of(userId), type)
        return ResponseHelper.success(ScheduleListResponse.of(schedules))
    }
    @DeleteMapping("")
    fun deleteSchedule(
        @RequestHeader("userId") userId: String
    ){

    }
}