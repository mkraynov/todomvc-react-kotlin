package utils

import kotlin.browser.localStorage
import kotlin.js.Json
import kotlin.js.Math.random
import kotlin.math.truncate

external class Number(value: kotlin.Number) {
    fun toString(radix: Int): String
}

fun uuid(): String {
    var uuid = "";

    for (i in 0..32) {
        val random = truncate(random() * 16).toInt()

        if (setOf(8, 12, 16, 20).contains(i)) {
            uuid += "-"
        }

        uuid += Number(when (i) {
            12 -> 4
            16 -> random.and(3).or(8)
            else -> random
        }).toString(16)
    }

    return uuid
}

fun pluralize(count: Int, word: String): String {
    return when (count) {
        1 -> word
        else -> word + "s"
    }
}

fun store(namespace: String): Json {
    val store = localStorage.getItem(namespace)
    return when (store) {
        null -> JSON.parse("[]")
        else -> JSON.parse(store)
    }
}

fun store(namespace: String, data: Json) {
    localStorage.setItem(namespace, JSON.stringify(data))
}

fun extend() {
    TODO("I don't need it in Kotlin")
}