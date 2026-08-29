package com.lunya.assistant.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class NanaBananaApi(private val apiKey: String, private val client: OkHttpClient = OkHttpClient()) {
    private val base = "https://api.nanobananaapi.ai/api/v1"

    suspend fun generate2(prompt: String, referenceUrls: List<String> = emptyList()): String = withContext(Dispatchers.IO) {
        require(apiKey.isNotBlank()) { "API key is empty" }
        val payload = JSONObject().apply {
            put("prompt", prompt)
            put("imageUrls", JSONArray(referenceUrls))
            put("aspectRatio", "1:1")
            put("resolution", "1K")
            put("googleSearch", false)
            put("outputFormat", "png")
        }
        val request = Request.Builder().url("$base/nanobanana/generate-2")
            .header("Authorization", "Bearer $apiKey")
            .post(payload.toString().toRequestBody("application/json".toMediaType())).build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw IllegalStateException("Nana Banana HTTP ${response.code}")
            val taskId = JSONObject(body).optJSONObject("data")?.optString("taskId").orEmpty()
            if (taskId.isBlank()) throw IllegalStateException("Nana Banana did not return taskId")
            taskId
        }
    }

    suspend fun getTask(taskId: String): NanaTaskResult = withContext(Dispatchers.IO) {
        val encoded = java.net.URLEncoder.encode(taskId, "UTF-8")
        val request = Request.Builder().url("$base/nanobanana/record-info?taskId=$encoded")
            .header("Authorization", "Bearer $apiKey").get().build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw IllegalStateException("Nana Banana HTTP ${response.code}")
            val json = JSONObject(body)
            val data = json.optJSONObject("data")
            val successFlag = data?.optInt("successFlag", -1) ?: -1
            val resultUrl = data?.optJSONObject("response")?.optString("resultImageUrl")
            NanaTaskResult(taskId, successFlag, resultUrl, body)
        }
    }
}

data class NanaTaskResult(val taskId: String, val successFlag: Int, val resultImageUrl: String?, val rawJson: String)
