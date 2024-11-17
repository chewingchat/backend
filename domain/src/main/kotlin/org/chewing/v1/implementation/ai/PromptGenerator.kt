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
            "다음은 ${friendName.firstName + friendName.lastName}님의 최근 피드 정보입니다. 텍스트와 이미지 설명이 포함되어 있습니다.",
        ),
    )
        .plus(
            feeds.flatMap { feed ->
                sequenceOf(
                    TextPrompt.of(feed.feed.topic),
                    TextPrompt.of("📅 ${feed.feed.uploadAt.format(dateFormatter) + "에 올린 사진 📸"}"),
                )
                    .plus(feed.feedDetails.map { detail -> ImagePrompt.of(detail.media.url) })
            },
        ).toList()
        .plus(
            listOf(
                TextPrompt.of(
                    "이 정보를 기반으로 ${friendName.firstName + friendName.lastName}님의 근황을 한글로 각각 주제당 3줄로 요약해주세요. " +
                        "마지막에는 친구와의 대화를 위해 필요한 주제도 함께 알려주세요. " +
                        "출력 형식은 아래 예시를 참고하여 다음과 같이 해주세요:\n\n" +
                        "```\n" +
                        "시간: 8월 1일 오전 7시\n" +
                        "근황: 아침 햇살을 맞으며 하루를 시작하고 있습니다. 상쾌한 아침 기운으로 기분이 좋습니다. 새로운 하루를 설레는 마음으로 맞이하고 있습니다.\n\n" +
                        "시간: 8월 3일 오후 12시\n" +
                        "근황: 회사 근처 식당에서 동료들과 함께 점심 식사를 즐기고 있습니다. 여유로운 시간을 보내며 동료들과의 유대감을 강화하고 있습니다. 맛있는 음식을 함께 나누며 행복한 시간을 보내고 있습니다.\n" +
                        "```\n\n" +
                        "**대화를 위한 주제 추천:**\n" +
                        "1. 아침 루틴과 하루 계획에 대해 이야기해보세요.\n" +
                        "2. 동료들과의 점심 시간 경험을 공유해보세요.\n" +
                        "3. 두바이 초콜릿에 대한 관심과 맛있는 초콜릿 추천을 나눠보세요.\n",
                ),

            ),
        )

    fun generateSearchChatPrompt(keyWord: String): List<Prompt> = sequenceOf(
        TextPrompt.of(
            "다음은 최근 채팅 검색에 대한 정보입니다. 텍스트에 검색 대한 정보가 포함되어 있습니다.",
        ),
        TextPrompt.of("검색어: $keyWord"),
    ).toList()
}
