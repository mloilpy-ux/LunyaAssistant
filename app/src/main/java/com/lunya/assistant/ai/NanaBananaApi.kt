package com.lunya.assistant.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

/** Minimal Nana Banana API client for task lookup.
 * The API key is intentionally supplied at runtime and is never committed to the repository.
 */
class NanaBananaApi(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    suspend fun getTask(taskId: String): NanaTaskResult = withContext(Dispatchers.IO) {
        require(apiKey.isNotBlank()) { "API key is empty" }
        require(taskId.isNotBlank()) { "taskId is empty" }

        val request = Request.Builder()
            .url("https://api.nanobananaapi.ai/api/v1/nanobanana/record-info?taskId=${java.net.URLEncoder.encode(taskId, "UTF-8")}")
            .header("Authorization", "Bearer $apiKey")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw IllegalStateException("Nana Banana HTTP ${response.code}: $body")
            }
            val json = JSONObject(body)
            val imageUrl = findString(json, "resultImageUrl")
            val status = findString(json, "status") ?: findString(json, "taskStatus")
            NanaTaskResult(taskId, status, imageUrl, body)
        }
    }

    private fun findString(value: Any?, target: String): String? {
        when (value) {
            is JSONObject -> {
                if (value.has(target) && !value.isNull(target)) return value.optString(target)
                val keys = value.keys()
                while (keys.hasNext()) {
                    val found = findString(value.opt(keys.next()), target)
                    if (!found.isNullOrBlank()) return found
                }
            }
            is JSONArray -> {
                for (i in 0 until value.length()) {
                    val found = findString(value.opt(i), target)
                    if (!found.isNullOrBlank()) return found
                }
            }
        }
        return null
    }
}

data class NanaTaskResult(
    val taskId: String,
    val status: String?,
    val resultImageUrl: String?,
    val rawJson: String
)
