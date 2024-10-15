package org.chewing.v1.controller.user

import org.chewing.v1.dto.request.ScheduleRequest
import org.chewing.v1.dto.response.chat.ChatRoomIdResponse
import org.chewing.v1.dto.response.schedule.ScheduleListResponse
import org.chewing.v1.model.schedule.ScheduleType
import org.chewing.v1.response.SuccessCreateResponse
import org.chewing.v1.response.SuccessOnlyResponse
import org.chewing.v1.service.ScheduleService
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/schedule")
class UserScheduleController(
    private val scheduleService: ScheduleService
) {
    @GetMapping("")
    fun getSchedule(
        @RequestAttribute("userId") userId: String,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int
    ): SuccessResponseEntity<ScheduleListResponse> {
        val type = ScheduleType.of(year, month)
        val schedules = scheduleService.fetches(userId, type)
        return ResponseHelper.success(ScheduleListResponse.of(schedules))
    }

    @DeleteMapping("")
    fun deleteSchedule(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: ScheduleRequest.Delete
    ): SuccessResponseEntity<SuccessCreateResponse> {
        val scheduleId = request.toScheduleId()
        scheduleService.remove(scheduleId)
        return ResponseHelper.successCreate(ChatRoomIdResponse.from(roomId))
    }

    @PostMapping("")
    fun addSchedule(
        @RequestAttribute("userId") userId: String,
        @RequestBody request: ScheduleRequest.Add
    ): SuccessResponseEntity<SuccessOnlyResponse> {
        scheduleService.create(userId, request.toScheduleTime(), request.toScheduleContent())
        return ResponseHelper.successOnly()
    }
}