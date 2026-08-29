package com.lunya.assistant.system

import android.content.Context
import android.util.Log
import com.lunya.assistant.animation.Emotion
import com.lunya.assistant.animation.LunyaAnimationDirector

/**
 * Makes Lunya aware of foreground apps and react to them.
 * Uses Accessibility / UsageStats style signals (fed from LunyaAccessibilityService).
 */
class AppAwarenessEngine(
    private val context: Context,
    private val director: LunyaAnimationDirector
) {

    companion object {
        private const val TAG = "AppAwareness"
    }

    data class AppProfile(
        val packageHint: String,
        val displayName: String,
        val emotion: Emotion,
        val phrase: String,
        val iconResHint: String = ""
    )

    private val profiles = listOf(
        AppProfile("youtube", "YouTube", Emotion.EXCITED, "Ооо, видосики! Можно вместе смотреть?"),
        AppProfile("chrome", "Chrome", Emotion.CURIOUS, "Серфим интернет? Я помогу искать!"),
        AppProfile("firefox", "Firefox", Emotion.CURIOUS, "Браузер открыт. Что ищем?"),
        AppProfile("messaging", "Messages", Emotion.CURIOUS, "Новое сообщение? Прочитать вслух?"),
        AppProfile("telegram", "Telegram", Emotion.HAPPY, "Телега! Кто-то написал?"),
        AppProfile("whatsapp", "WhatsApp", Emotion.HAPPY, "WhatsApp активен~"),
        AppProfile("discord", "Discord", Emotion.EXCITED, "Дискорд! Войс или чат?"),
        AppProfile("instagram", "Instagram", Emotion.LOVE, "Инста... смотрим милые картинки?"),
        AppProfile("tiktok", "TikTok", Emotion.EXCITED, "ТикТок! Не залипни без меня."),
        AppProfile("spotify", "Spotify", Emotion.HAPPY, "Музыка! Я буду танцевать в такт."),
        AppProfile("youtube.music", "YT Music", Emotion.HAPPY, "Музыкальный вайб включён."),
        AppProfile("netflix", "Netflix", Emotion.LOVE, "Киношка? Я рядом, тихонько."),
        AppProfile("twitch", "Twitch", Emotion.EXCITED, "Стрим! Давай болеть вместе."),
        AppProfile("steam", "Steam", Emotion.OVERCLOCKED, "Игры! Режим геймера активирован."),
        AppProfile("genshin", "Genshin", Emotion.EXCITED, "Геншин... удачи с 50/50!"),
        AppProfile("honkai", "Honkai", Emotion.EXCITED, "Хонкай! Вайбы качаем."),
        AppProfile("maps", "Maps", Emotion.CURIOUS, "Куда путь держим?"),
        AppProfile("camera", "Camera", Emotion.SURPRISED, "Камера! Сфоткай меня тоже~"),
        AppProfile("settings", "Settings", Emotion.FOCUSED, "Настройки... осторожнее там."),
        AppProfile("dialer", "Phone", Emotion.CURIOUS, "Звонок? Я могу напомнить, кто это."),
        AppProfile("clock", "Clock", Emotion.SLEEPY, "Будильник? Не забудь про сон."),
        AppProfile("calendar", "Calendar", Emotion.FOCUSED, "Календарь — проверю твои дела."),
        AppProfile("gmail", "Gmail", Emotion.CURIOUS, "Почта. Важное или спам?"),
        AppProfile("docs", "Docs", Emotion.FOCUSED, "Документы. Режим концентрации."),
        AppProfile("reddit", "Reddit", Emotion.CURIOUS, "Реддит... осторожно с кроличьими норами.")
    )

    private var lastPackage: String? = null
    private var lastChangeMs = 0L

    /** Call when accessibility reports a new foreground window/package. */
    fun onForegroundApp(packageName: String, appLabel: String? = null) {
        if (packageName == lastPackage) return
        if (System.currentTimeMillis() - lastChangeMs < 800) return // debounce

        lastPackage = packageName
        lastChangeMs = System.currentTimeMillis()

        val lower = packageName.lowercase()
        val profile = profiles.find { lower.contains(it.packageHint) }

        if (profile != null) {
            director.currentEmotion = profile.emotion
            director.triggerNotification() // ear perk
            Log.d(TAG, "App detected: ${profile.displayName} -> ${profile.emotion} | ${profile.phrase}")
            onAppReaction?.invoke(profile)
        } else {
            director.currentEmotion = Emotion.CURIOUS
            Log.d(TAG, "Unknown app: $packageName ($appLabel)")
            onAppReaction?.invoke(
                AppProfile(packageName, appLabel ?: packageName, Emotion.CURIOUS, "Новое приложение... что это?")
            )
        }
    }

    var onAppReaction: ((AppProfile) -> Unit)? = null

    fun getPhraseForCurrentApp(): String? {
        val pkg = lastPackage ?: return null
        return profiles.find { pkg.lowercase().contains(it.packageHint) }?.phrase
    }
}
