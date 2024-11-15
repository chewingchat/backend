package org.chewing.v1.implementation.ai

import org.chewing.v1.external.ExternalAiClient
import org.chewing.v1.model.ai.Prompt
import org.chewing.v1.util.AsyncJobExecutor
import org.springframework.stereotype.Component

@Component
class AiSender(
    private val asyncJobExecutor: AsyncJobExecutor,
    private val externalAiClient: ExternalAiClient,
) {
    fun sendAiPrompt(prompts: List<Prompt>): String = asyncJobExecutor.executeAsyncReturnJob(prompts) {
        externalAiClient.prompt(prompts)
    }
}
