package org.chewing.v1.implementation.ai

import org.chewing.v1.model.ai.ImagePrompt
import org.chewing.v1.model.ai.Prompt
import org.chewing.v1.model.ai.TextPrompt
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.user.UserName
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter
import java.util.Locale

@Component
class PromptGenerator {

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일 a h시", Locale.KOREAN)

    fun generateRecentSummaryPrompt(friendName: UserName, feeds: List<Feed>): List<Prompt> = sequenceOf(
        TextPrompt.of(
            "다음은 ${friendName.firstName + friendName.lastName}님의 최근 피드 정보입니다." +
                " 이 정보를 기반으로 친구님의 근황을 각각 topic 당 3줄로 요약해주세요. 이때 출력은은 시간\n 요약이 나오도록해주세요." +
                " 이떄 친구와의 대화를 위해 필요한 주제도 같이 추가해주세요",
        ),
    )
        .plus(
            feeds.flatMap { feed ->
                sequenceOf(
                    TextPrompt.of("📅 ${feed.feed.uploadAt.format(dateFormatter)}\n${feed.feed.topic}"),
                )
                    .plus(feed.feedDetails.map { detail -> ImagePrompt.of(detail.media.url) })
            },
        ).toList()
}
