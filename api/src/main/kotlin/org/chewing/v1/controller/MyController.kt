package org.chewing.v1.controller

import org.chewing.v1.dto.response.my.MyCommentResponse
import org.chewing.v1.facade.MyFacade
import org.chewing.v1.util.ResponseHelper
import org.chewing.v1.util.SuccessResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/my")
class MyController(
    private val myFacade: MyFacade,
) {
    @GetMapping("/comment")
    fun getMyCommentedFeed(
        @RequestAttribute("userId") userId: String,
    ): SuccessResponseEntity<MyCommentResponse> {
        val myCommentedInfo = myFacade.getFeedUserCommented(userId)
        //성공 응답 200 반환
        return ResponseHelper.success(MyCommentResponse.of(myCommentedInfo))
    }
}