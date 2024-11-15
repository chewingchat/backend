package org.chewing.v1.controller.ai

import org.chewing.v1.dto.response.ai.AiResponse
import org.chewing.v1.facade.AiFacade
import org.chewing.v1.model.ai.DateTarget
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai")
class AiController(
    private val aiFacade: AiFacade,
) {
    @GetMapping("/friend/summary")
    fun getFriendSummary(
        @RequestAttribute("userId") userId: String,
        @RequestParam("friendId") friendId: String,
        @RequestParam("targetDate") dateTarget: DateTarget,
    ): SuccessResponseEntity<AiResponse> {
        val result = aiFacade.getAiRecentSummary(userId, friendId, dateTarget)
        return ResponseHelper.success(AiResponse.of(result))
    }
}
