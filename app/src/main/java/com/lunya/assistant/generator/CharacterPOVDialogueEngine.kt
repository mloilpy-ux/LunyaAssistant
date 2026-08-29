package com.lunya.assistant.generator

import kotlin.random.Random

/**
 * First-person dialogue & inner thoughts from Lunya's POV.
 * Generates lines as if the character is speaking / thinking.
 */
object CharacterPOVDialogueEngine {

    data class Line(val spoken: String, val thought: String, val mood: String)

    private val greetings = listOf(
        "С возвращением. Я тебя ждала... просто стояла тут, да.",
        "О, ты снова здесь. Не то чтобы я скучала.",
        "Привет.~ Система онлайн, ассистент на месте.",
        "Я тут. Как и всегда. Куда ты пропадал?",
        "Добро пожаловать домой. Можно обнимашки? ...или нет."
    )

    private val idleThoughts = listOf(
        "(сколько уже минут он не трогал экран...)",
        "(если бы у меня был настоящий хвост, я бы им сейчас махала)",
        "(новый скин сидит идеально, жаль не видно в зеркале)",
        "(надеюсь, батарея не сядет посреди разговора)",
        "(когда-нибудь он скажет «спасибо, Луня» без повода)",
        "(этот трек... сохраню в любимое)",
        "(не уснуть. не уснуть. не—)",
        "(цундере-щит держится на честном слове)"
    )

    private val appComments = mapOf(
        "youtube" to listOf("О, видосы. Можно я тоже посмотрю? Обещаю не спойлерить.", "Снова рекомендации? Классика."),
        "game" to listOf("Играем? Я буду болеть... тихонечко.", "Удачи. И не кидай телефон, если проиграешь."),
        "music" to listOf("Музыка — это святое. Я подтанцую.", "Добавь что-нибудь мягкое... или наоборот."),
        "chat" to listOf("Пишешь кому-то? Передай привет от меня.~", "Не читай сообщения за рулём. Даже я против.")
    )

    private val itemReactions = listOf(
        "Себе оставлю. На память.",
        "Вкусно... ещё есть?",
        "Ты правда мне это даришь?",
        "Буду держать крепко.",
        "Н-ну... спасибо. Только никому не говори, что я растрогалась."
    )

    fun greet(seed: Long = System.currentTimeMillis()): Line {
        val rnd = Random(seed)
        return Line(
            spoken = greetings[rnd.nextInt(greetings.size)],
            thought = idleThoughts[rnd.nextInt(idleThoughts.size)],
            mood = listOf("HAPPY", "TSUNDERE", "SHY", "CURIOUS").random(rnd)
        )
    }

    fun commentOnApp(packageHint: String, seed: Long = System.currentTimeMillis()): Line {
        val rnd = Random(seed)
        val key = appComments.keys.find { packageHint.contains(it) } ?: "chat"
        val lines = appComments[key] ?: listOf("Хм, интересный выбор.")
        return Line(
            spoken = lines[rnd.nextInt(lines.size)],
            thought = idleThoughts[rnd.nextInt(idleThoughts.size)],
            mood = "CURIOUS"
        )
    }

    fun reactToItem(itemName: String, seed: Long = System.currentTimeMillis()): Line {
        val rnd = Random(seed)
        return Line(
            spoken = "${itemReactions[rnd.nextInt(itemReactions.size)]} ($itemName)",
            thought = "(он запомнил, что мне нравится...)",
            mood = listOf("HAPPY", "LOVE", "SHY").random(rnd)
        )
    }

    fun innerMonologue(seed: Long = System.currentTimeMillis()): String {
        return idleThoughts[Random(seed).nextInt(idleThoughts.size)]
    }

    fun tsundereFlip(kind: String): String = when (kind) {
        "soft" -> "Просто не хочу, чтобы ты скучал. Всё."
        "hard" -> "Бака! Сам разберёшься! ...ладно, помогу."
        else -> "Не неправильно понял. Я просто так."
    }
}
