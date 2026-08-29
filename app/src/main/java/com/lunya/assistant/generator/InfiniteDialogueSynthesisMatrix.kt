package com.lunya.assistant.generator

import kotlin.random.Random

/**
 * Infinite dialogue synthesis for Lunya (v8).
 */
object InfiniteDialogueSynthesisMatrix {

    private val greetings = listOf(
        "Ня~ хозяин вернулся!",
        "Оверклок активирован... привет.",
        "Banana mode online! Что будем делать?",
        "Тсундере-режим: ...не то чтобы я ждала.",
        "Система готова. Прикажи."
    )

    private val moods = listOf("bored", "tsundere", "overclocked", "sleepy", "hyper")

    fun synthesizeGreeting(seed: Long = System.currentTimeMillis()): String {
        val rnd = Random(seed)
        return greetings[rnd.nextInt(greetings.size)]
    }

    fun randomMood(): String = moods.random()
}
