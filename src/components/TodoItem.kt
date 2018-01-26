package components

import index.ENTER_KEYCODE
import index.ESCAPE_KEYCODE
import kotlinx.html.InputType
import kotlinx.html.js.*
import model.TodoModel
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label

interface TodoItemProps : RProps {
    var editing: Boolean
    var todo: TodoModel

    var onCancel: () -> Unit
    var onDestroy: (todo: TodoModel) -> Unit
    var onEdit: (todo: TodoModel) -> Unit
    var onSave: (todo: TodoModel, value: String) -> Unit
    var onToggle: (todo: TodoModel) -> Unit
}

interface TodoItemState : RState {
    var editText: String
}

class TodoItem(props: TodoItemProps) : RComponent<TodoItemProps, TodoItemState>(props) {
    fun handleSubmit() {
        val value = state.editText.trim()
        if (value != EMPTY_STRING) {
            props.onSave(props.todo, value)
            setState { editText = value }
        } else {
            props.onDestroy(props.todo)
        }
    }

    override fun RBuilder.render() {
        div {
            div("view") {
                input(InputType.checkBox, classes = "toggle") {
                    attrs {
                        checked = props.todo.completed
                        onChangeFunction = {
                            props.onToggle(props.todo)
                        }
                    }
                }
                label {
                    +props.todo.title
                    attrs {
                        onDoubleClickFunction = {
                            props.onEdit(props.todo);
                            setState { editText = props.todo.title }
                        }
                    }
                }
                button(classes = "destroy") {
                    attrs {
                        onClickFunction = { props.onDestroy(props.todo) }
                    }
                }
            }
            input(type = InputType.text, classes = "edit") {
                attrs {
                    value = state.editText
                    onBlurFunction = { handleSubmit() }
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        if (props.editing) {
                            setState { editText = target.value }
                        }
                    }
                    onKeyDownFunction = {
                        val keyCode: Int = it.asDynamic().keyCode
                        if (keyCode == ESCAPE_KEYCODE) {
                            setState { editText = props.todo.title }
                            props.onCancel()
                        } else if (keyCode == ENTER_KEYCODE) {
                            handleSubmit()
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.todoItem(editing: Boolean,
                      todo: TodoModel,
                      onCancel: () -> Unit,
                      onDestroy: (todo: TodoModel) -> Unit,
                      onEdit: (todo: TodoModel) -> Unit,
                      onSave: (todo: TodoModel, value: String) -> Unit,
                      onToggle: (todo: TodoModel) -> Unit
) = child(TodoItem::class) {
    attrs.editing = editing
    attrs.todo = todo

    attrs.onCancel = onCancel
    attrs.onDestroy = onDestroy
    attrs.onEdit = onEdit
    attrs.onSave = onSave
    attrs.onToggle = onToggle
}
