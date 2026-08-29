package com.lunya.assistant.ai

import android.content.Context
import com.lunya.assistant.wardrobe.LunyaWardrobe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LunyaReactionEngine(context: Context, private val apiKey: String) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences("lunya_ai", Context.MODE_PRIVATE)
    private val wardrobe = LunyaWardrobe(appContext)
    private val api = NanaBananaApi(apiKey)
    private val poller = NanaBananaTaskPoller(api)
    private val identity = """
Lunya is a male anthropomorphic deer femboy. Strictly deer, never cat, fox or another species. Preserve the supplied canonical Lunya reference exactly: recognizable face, deer anatomy, proportions, silhouette and established palette. No breasts or chest volume. Do not redesign the character. Keep the selected wardrobe outfit.
""".trimIndent()
    private val events = mapOf(
        "entry" to "warm happy greeting, waving, sparkly eyes, tiny hearts",
        "app_changed" to "curious glance at the newly opened app, playful surprise",
        "tap" to "playful excited reaction after being tapped",
        "pet" to "very happy affectionate reaction to being petted",
        "long_press" to "dramatic energetic overclock reaction",
        "notification" to "curious surprised reaction to a notification",
        "success" to "proud celebration, stars and confetti",
        "error" to "cute worried apologetic reaction",
        "idle" to "cozy sleepy idle, gentle yawn",
        "love" to "shy affectionate reaction, blush and hearts",
        "laugh" to "playful laughing reaction",
        "angry" to "cartoonishly annoyed puffed cheeks",
        "drink" to "happy energetic reaction while drinking",
        "bored" to "cute bored reaction, looking around",
        "sad" to "soft sad reaction, gentle downcast expression",
        "excited" to "extremely excited celebration",
        "sleep" to "sleepy cozy reaction with closed eyes"
    )

    suspend fun reaction(event: String, canonicalTaskId: String? = null): String {
        prefs.edit().putString("api_key", apiKey).apply()
        val task = canonicalTaskId?.takeIf(String::isNotBlank)
            ?: prefs.getString("canonical_task_id", "1e099185c5d9ac033ce9678225fb46a4")!!
        if (prefs.getString("canonical_reference_url", null).isNullOrBlank()) establishCanonicalReference(task)
        val reference = prefs.getString("canonical_reference_url", null)
        val outfit = wardrobe.current()
        val prompt = buildString {
            append(identity)
            append("\nCurrent outfit: ${outfit.setName}. ${outfit.description}.")
            append("\nCreate ONE transparent-background PNG sticker/emoji of Lunya.")
            append("\nReaction: ${events[event] ?: events["idle"]}.")
            append("\nFull character, centered, clean silhouette, expressive face, polished sticker illustration, no text, no watermark, transparent background.")
        }
        val generatedTask = api.generate2(prompt, listOfNotNull(reference))
        return poller.awaitImage(generatedTask).also { prefs.edit().putString("last_reaction_url", it).apply() }
    }

    suspend fun establishCanonicalReference(taskId: String): String = withContext(Dispatchers.IO) {
        val url = poller.awaitImage(taskId)
        prefs.edit().putString("canonical_task_id", taskId).putString("canonical_reference_url", url).apply()
        url
    }

    fun nextOutfit() = wardrobe.next()
    fun currentOutfit() = wardrobe.current()
}
