package model

import utils.store
import utils.uuid

class TodosModel(val key: String = "todomvc-react-kotlin") {
    var todos: List<TodoModel> = store(key)

    var onChanges: List<() -> Unit> = emptyList()

    fun subscribe(onChange: () -> Unit) {
        onChanges = onChanges.plus(onChange)
    }

    fun inform() {
        store(key, this)
        onChanges.forEach { it() }
    }

    fun addTodo(title: String) {
        todos = todos.plus(TodoModel(uuid(), title))
        inform()
    }

    fun toggleAll(completed: Boolean) {
        todos = todos.map {
            it.copy(completed = completed)
        }
        inform()
    }

    fun toggle(id: String) {
        todos = todos.map {
            when (it.id) {
                id -> it.copy(completed = !it.completed)
                else -> it
            }
        }
        inform()
    }

    fun destroy(todo: TodoModel) {
        todos = todos.filter { it !== todo }
        inform()
    }

    fun save(todo: TodoModel, text: String) {
        todos = todos.map {
            when (it) {
                todo -> todo.copy(title = text)
                else -> it
            }
        }
        inform()
    }

    fun clearCompleted() {
        todos = todos.filter { !it.completed }
        inform()
    }

    fun getCompletedCount(): Int {
        return todos.filter { it.completed }.size
    }

    override fun toString(): String {
        return JSON.stringify(todos)
    }
}