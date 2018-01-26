package model

import utils.uuid

class TodosModel {
    var todos: List<TodoModel> = emptyList()

    var onChanges: List<Function<Any>> = emptyList()

    fun subscribe(onChange: Function<Any>) {
        onChanges = onChanges.plus(onChange)
    }

    fun inform() {
        onChanges.forEach { it.asDynamic()() }
    }

    fun addTodo(title: String) {
        todos = todos.plus(TodoModel(uuid(), title))
        inform()
    }

    fun toggleAll(completed: Boolean) {
        todos = todos.map {
            it.toggle(completed)
        }
        inform()
    }

    fun toggle(id: String) {
        todos = todos.map {
            when (it.id) {
                id -> it.toggle(!it.completed)
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
                todo -> todo.changeTitle(text)
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