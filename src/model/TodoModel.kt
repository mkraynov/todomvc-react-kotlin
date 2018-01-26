package model

class TodoModel(val id: String, var title: String, var completed: Boolean = false) {
    fun toggle(completed: Boolean): TodoModel {
        return TodoModel(
                id = id,
                title = title,
                completed = completed
        )
    }

    fun changeTitle(title: String): TodoModel {
        return TodoModel(
                id = id,
                title = title,
                completed = completed
        )
    }
}
