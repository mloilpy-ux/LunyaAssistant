package com.lunya.assistant.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class NanaBananaApi(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    suspend fun getTask(taskId: String): NanaTaskResult = withContext(Dispatchers.IO) {
        require(apiKey.isNotBlank()) { "API key is empty" }
        require(taskId.isNotBlank()) { "taskId is empty" }
        val encoded = java.net.URLEncoder.encode(taskId, "UTF-8")
        val request = Request.Builder()
            .url("https://api.nanobananaapi.ai/api/v1/nanobanana/record-info?taskId=$encoded")
            .header("Authorization", "Bearer $apiKey")
            .get().build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw IllegalStateException("Nana Banana HTTP ${response.code}")
            val json = JSONObject(body)
            NanaTaskResult(taskId, findString(json, "status") ?: findString(json, "taskStatus"), findString(json, "resultImageUrl"), body)
        }
    }

    private fun findString(value: Any?, target: String): String? = when (value) {
        is JSONObject -> {
            if (value.has(target) && !value.isNull(target)) value.optString(target)
            else value.keys().asSequence().mapNotNull { findString(value.opt(it), target) }.firstOrNull()
        }
        is JSONArray -> (0 until value.length()).asSequence().mapNotNull { findString(value.opt(it), target) }.firstOrNull()
        else -> null
    }
}

data class NanaTaskResult(val taskId: String, val status: String?, val resultImageUrl: String?, val rawJson: String)
