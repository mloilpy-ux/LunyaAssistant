package com.lunya.assistant.ai

import kotlinx.coroutines.delay

class NanaBananaTaskPoller(private val api: NanaBananaApi) {
    suspend fun awaitImage(taskId: String, attempts: Int = 40, intervalMs: Long = 3000L): String {
        repeat(attempts) { attempt ->
            val result = api.getTask(taskId)
            if (result.successFlag == 1) return result.resultImageUrl?.takeIf { it.isNotBlank() }
                ?: error("Task succeeded but resultImageUrl is missing")
            if (result.successFlag == 2 || result.successFlag == 3) error("Nana Banana generation failed")
            if (attempt < attempts - 1) delay(intervalMs)
        }
        error("Nana Banana generation timed out")
    }
}
