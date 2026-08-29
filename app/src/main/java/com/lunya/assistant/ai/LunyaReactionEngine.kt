package com.lunya.assistant.ai

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Creates a fresh sticker/reaction while preserving Lunya's canonical visual identity. */
class LunyaReactionEngine(private val context: Context, private val apiKey: String) {
    private val prefs = context.getSharedPreferences("lunya_ai", Context.MODE_PRIVATE)
    private val api = NanaBananaApi(apiKey)
    private val poller = NanaBananaTaskPoller(api)

    private val identity = """
Lunya is a male anthropomorphic deer femboy character, strictly deer, not cat or fox. Preserve the exact established Lunya design from the supplied reference: solid light-blue fur with no spots and no white fur; violet nose, violet inner ears and violet eyes; long green-to-yellow neon gradient hair; short fluffy deer tail matching the hair; brown antlers and brown deer hooves; black hoodie with neon-green top; choker with green heart pendant and collar marked L. Keep the same face, proportions, species, colors and recognizable silhouette. No breasts/chest volume.
""".trimIndent()

    private val events = mapOf(
        "entry" to "happy greeting, waving, sparkly eyes, tiny hearts, sticker pose",
        "success" to "proud happy celebration, thumbs-up-like hoof gesture, stars and confetti",
        "error" to "cute worried reaction, sweat drop, apologetic expression, small glitch symbols",
        "notification" to "curious surprised reaction looking toward a notification bubble",
        "idle" to "cozy sleepy idle reaction, soft yawn, small floating hearts",
        "love" to "shy affectionate reaction, blushing face, hearts, playful smile",
        "laugh" to "playful laughing reaction, closed happy eyes, bouncing pose",
        "angry" to "cartoonishly annoyed reaction, puffed cheeks, tiny harmless anger marks"
    )

    suspend fun reaction(event: String): String {
        val task = prefs.getString("canonical_task_id", "1e099185c5d9ac033ce9678225fb46a4")!!
        val reference = prefs.getString("canonical_reference_url", null)
        val referenceUrls = reference?.let { listOf(it) } ?: emptyList()
        val prompt = "$identity\nCreate ONE clean transparent-background emoji/sticker reaction of Lunya. Event: ${events[event] ?: events["idle"]}. Centered full character, readable silhouette, expressive face, polished sticker illustration, no text, no watermark, transparent background."
        val generatedTask = api.generate2(prompt, referenceUrls)
        return poller.awaitImage(generatedTask).also {
            prefs.edit().putString("last_reaction_url", it).apply()
        }
    }

    suspend fun establishCanonicalReference(taskId: String): String = withContext(Dispatchers.IO) {
        val url = poller.awaitImage(taskId)
        prefs.edit().putString("canonical_task_id", taskId).putString("canonical_reference_url", url).apply()
        url
    }
}
