package org.chewing.v1.controller

import org.chewing.v1.dto.request.ScheduleRequest
import org.chewing.v1.dto.response.schedule.ScheduleListResponse
import org.chewing.v1.model.schedule.ScheduleType
import org.chewing.v1.model.User
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.ScheduleService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Month
import java.time.Year

@RestController
@RequestMapping("/api/schedule")
class ScheduleController(
    private val scheduleService: ScheduleService
) {
    @GetMapping("")
    fun getSchedule(
        @RequestHeader("userId") userId: String,
        @RequestParam("year") year: Year,
        @RequestParam("month") month: Month
    ): SuccessResponseEntity<ScheduleListResponse> {
        val type = ScheduleType.of(year, month)
        val schedules = scheduleService.fetches(userId, type)
        return ResponseHelper.success(ScheduleListResponse.of(schedules))
    }

    @DeleteMapping("")
    fun deleteSchedule(
        @RequestHeader("userId") userId: String,
        @RequestBody request: ScheduleRequest.Delete
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val scheduleId = request.toScheduleId()
        scheduleService.remove(scheduleId)
        return ResponseHelper.successCreate()
    }

    @PostMapping("")
    fun addSchedule(
        @RequestHeader("userId") userId: String,
        @RequestBody request: ScheduleRequest.Add
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        scheduleService.make(userId, request.toScheduleTime(), request.toScheduleContent())
        return ResponseHelper.successOnly()
    }
}