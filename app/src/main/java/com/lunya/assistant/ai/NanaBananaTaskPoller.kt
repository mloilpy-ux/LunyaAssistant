package com.lunya.assistant.ai

import kotlinx.coroutines.delay

/** Polls a completed generation task until an image URL appears. */
class NanaBananaTaskPoller(private val api: NanaBananaApi) {
    suspend fun awaitImage(taskId: String, attempts: Int = 30, intervalMs: Long = 2000L): String {
        repeat(attempts) { attempt ->
            val result = api.getTask(taskId)
            result.resultImageUrl?.takeIf { it.isNotBlank() }?.let { return it }
            if (attempt < attempts - 1) delay(intervalMs)
        }
        error("Nana Banana task did not return resultImageUrl within the polling window")
    }
}
