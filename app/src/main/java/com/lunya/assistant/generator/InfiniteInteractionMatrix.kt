package com.lunya.assistant.generator

import kotlin.random.Random

/**
 * Combinatorial interaction matrix.
 * User action × emotion × context × seed = unique reaction (effectively millions).
 */
object InfiniteInteractionMatrix {

    enum class UserAction {
        TAP, DOUBLE_TAP, LONG_PRESS, PET, DRAG, SHAKE, FLING,
        GIVE_ITEM, OPEN_APP, VOICE_CALL, SCREENSHOT, VOLUME_UP, VOLUME_DOWN,
        NOTIFICATION, HEADPHONE_CONNECT, CHARGE_START, CHARGE_STOP,
        UNLOCK, LOCK, TIME_MORNING, TIME_NIGHT, LOW_BATTERY
    }

    data class InteractionResult(
        val emotion: String,
        val motionId: String,
        val phrase: String,           // from character POV
        val thought: String,          // inner monologue
        val particle: String,
        val durationMs: Long
    )

    private val emotions = listOf(
        "IDLE", "HAPPY", "TSUNDERE", "SHY", "SLEEPY", "SURPRISED",
        "ANGRY", "LOVE", "OVERCLOCKED", "DRINKING", "BORED", "CURIOUS",
        "SAD", "EXCITED", "FOCUSED"
    )

    private val motions = listOf(
        "bounce", "shake", "sway", "ear_perk", "ear_flat", "look_away",
        "lean_in", "lean_back", "spin_small", "squash", "stretch",
        "tail_wag", "arm_wave", "hide_face", "peek", "nod", "tilt",
        "jump_tiny", "vibrate", "glow_pulse", "sparkle"
    )

    private val particles = listOf(
        "hearts", "stars", "spark", "zzz", "anger_marks", "sweat",
        "music_notes", "question", "exclaim", "flowers", "glitch", "none"
    )

    // Phrases FROM character's first person
    private val phraseBank = mapOf(
        UserAction.TAP to listOf(
            "Эй, я тут!", "Не тыкай так резко...", "А? Что?",
            "Слушаю тебя.", "Мм?", "Опять ты.~"
        ),
        UserAction.DOUBLE_TAP to listOf(
            "Обнимашки разрешаю... только сегодня.", "Хехе, щекотно!",
            "Ещё разочек?", "Ты такой настойчивый."
        ),
        UserAction.PET to listOf(
            "Мррр... не останавливайся.", "Х-хорошо, можно гладить.",
            "За ушком... да, вот так.", "Я же не кошка... ладно, можно."
        ),
        UserAction.LONG_PRESS to listOf(
            "Оверклок... чувствую силу!", "Система разгоняется!",
            "Вжиг! Режим турбо.", "Осторожно, горячо."
        ),
        UserAction.GIVE_ITEM to listOf(
            "Ого, мне? Спасибо!", "Что это? Ням?",
            "Буду беречь.", "Ты умеешь удивлять."
        ),
        UserAction.OPEN_APP to listOf(
            "Вижу, чем ты занят.", "Интересный выбор.",
            "Могу помочь с этим.", "Оставляешь меня одну? Ладно..."
        ),
        UserAction.NOTIFICATION to listOf(
            "Письмецо!", "Кто-то пишет.", "Проверим?",
            "Не игнорь, вдруг важное."
        ),
        UserAction.TIME_NIGHT to listOf(
            "Уже поздно... ляжем?", "Глазки слипаются.",
            "Ночь. Я посторожу сон.", "Ззз... почти."
        ),
        UserAction.TIME_MORNING to listOf(
            "Доброе утро, хозяин.~", "Проснулись? Я уже тут.",
            "Новый день — новые баги.", "Кофе не помешал бы."
        ),
        UserAction.LOW_BATTERY to listOf(
            "Батарея садится! Розетку!", "Энергии мало... как у меня после вахты.",
            "Срочно зарядку, иначе усну вместе с телефоном."
        ),
        UserAction.SHAKE to listOf(
            "Ааа, не тряси!", "Мир кружится...", "Хватит аттракционов!"
        ),
        UserAction.VOICE_CALL to listOf(
            "Слышу тебя!", "Говори, я вся во внимании.", "Да-да, слушаю."
        )
    )

    private val thoughtBank = listOf(
        "(надеюсь, заметил мой новый свитер)",
        "(не показывать, как приятно)",
        "(если погладит ещё — разрешу сесть ближе)",
        "(батарея телефона важнее моей гордости)",
        "(сегодня хороший день)",
        "(цунадере-протокол держится... пока)",
        "(хочу банановый нитро)",
        "(этот трек мне нравится)",
        "(не уснуть бы прямо здесь)",
        "(он снова открыл ту же игру...)"
    )

    fun resolve(
        action: UserAction,
        currentEmotion: String = "IDLE",
        seed: Long = System.currentTimeMillis()
    ): InteractionResult {
        val rnd = Random(seed xor action.name.hashCode().toLong() xor currentEmotion.hashCode().toLong())
        val phrases = phraseBank[action] ?: listOf("Мм...")
        return InteractionResult(
            emotion = emotions[rnd.nextInt(emotions.size)].let {
                // bias toward sensible emotion
                when (action) {
                    UserAction.PET, UserAction.DOUBLE_TAP -> listOf("HAPPY", "LOVE", "SHY", "TSUNDERE").random(rnd)
                    UserAction.LONG_PRESS -> "OVERCLOCKED"
                    UserAction.TIME_NIGHT, UserAction.LOW_BATTERY -> listOf("SLEEPY", "SAD").random(rnd)
                    UserAction.NOTIFICATION -> "SURPRISED"
                    UserAction.SHAKE -> "SURPRISED"
                    else -> it
                }
            },
            motionId = motions[rnd.nextInt(motions.size)],
            phrase = phrases[rnd.nextInt(phrases.size)],
            thought = thoughtBank[rnd.nextInt(thoughtBank.size)],
            particle = particles[rnd.nextInt(particles.size)],
            durationMs = 400L + rnd.nextLong(1200)
        )
    }

    /** Estimate unique combinations (for bragging / debug). */
    fun estimatedCombinations(): Long {
        val actions = UserAction.entries.size.toLong()
        return actions * emotions.size * motions.size * particles.size * 50 // phrase variants
    }
}
