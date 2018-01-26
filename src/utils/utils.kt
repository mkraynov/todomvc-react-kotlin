package utils

import model.TodoModel
import model.TodosModel
import kotlin.browser.localStorage
import kotlin.js.Math.random
import kotlin.math.truncate

external class Number(value: kotlin.Number) {
    fun toString(radix: Int): String
}

external class Array<T>() {
    fun concat(toConcat: Any?): Array<T>
    fun forEach(fn: (element: T, index: Int, array: Array<T>) -> Unit): Unit
}

fun Array<TodoModel>.asList(): List<TodoModel> {
    val list = mutableListOf<TodoModel>()
    this.forEach { todo, index, _ ->
        list.add(index, TodoModel(todo.id, todo.title, todo.completed)) }
    return list.toList()
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

fun store(namespace: String): List<TodoModel> {
    val store = localStorage.getItem(namespace)
    return when (store) {
        null -> emptyList()
        else -> Array<TodoModel>().concat(JSON.parse(store)).asList()
    }
}

fun store(namespace: String, model: TodosModel) {
    localStorage.setItem(namespace, model.toString())
}

fun extend() {
    TODO("I don't need it in Kotlin")
}