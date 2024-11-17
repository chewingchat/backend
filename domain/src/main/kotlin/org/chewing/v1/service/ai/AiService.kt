package org.chewing.v1.service.ai

import org.chewing.v1.implementation.ai.AiSender
import org.chewing.v1.implementation.ai.PromptGenerator
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.user.UserName
import org.springframework.stereotype.Service

@Service
class AiService(
    private val promptGenerator: PromptGenerator,
    private val aiSender: AiSender,
) {
    fun getAiRecentSummary(friendName: UserName, feeds: List<Feed>): String {
        val prompts = promptGenerator.generateRecentSummaryPrompt(friendName, feeds)
        return aiSender.sendAiPrompt(prompts)
    }
    fun getAiSearchChat(keyWord: String): String {
        val prompts = promptGenerator.generateSearchChatPrompt(keyWord)
        return aiSender.sendAiPrompt(prompts)
    }
}
