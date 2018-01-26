package components

import index.ENTER_KEYCODE
import index.ESCAPE_KEYCODE
import index.classNames
import kotlinext.js.Object
import kotlinx.html.InputType
import kotlinx.html.js.*
import model.TodoModel
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label
import kotlin.js.Json

interface TodoItemProps : RProps {
    var editing: Boolean
    var todo: TodoModel

    var onCancel: Function<Any>
    var onDestroy: Function<Any>
    var onEdit: Function<Any>
    var onSave: Function<Any>
    var onToggle: Function<Any>
}

interface TodoItemState : RState {
    var editText: String
}

class TodoItem(props: TodoItemProps) : RComponent<TodoItemProps, TodoItemState>(props) {
    fun handleSubmit() {
        val value = state.editText.trim()
        if (value != EMPTY_STRING) {
            props.onSave.asDynamic()(props.todo, value)
            setState { editText = value }
        } else {
            props.onDestroy.asDynamic()(props.todo)
        }
    }

    override fun RBuilder.render() {
        div {
            div("view") {
                input(InputType.checkBox, classes = "toggle") {
                    attrs {
                        checked = props.todo.completed
                        onChangeFunction = {
                            props.onToggle.asDynamic()(props.todo)
                        }
                    }
                }
                label {
                    +props.todo.title
                    attrs {
                        onDoubleClickFunction = {
                            props.onEdit.asDynamic()();
                            setState { editText = props.todo.title }
                        }
                    }
                }
                button(classes = "destroy") {
                    attrs {
                        onClickFunction = { props.onDestroy.asDynamic()(props.todo) }
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
                            props.onCancel.asDynamic()()
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
                      onCancel: Function<Any>,
                      onDestroy: Function<Any>,
                      onEdit: Function<Any>,
                      onSave: Function<Any>,
                      onToggle: Function<Any>
) = child(TodoItem::class) {
    attrs.editing = editing
    attrs.todo = todo

    attrs.onCancel = onCancel
    attrs.onDestroy = onDestroy
    attrs.onEdit = onEdit
    attrs.onSave = onSave
    attrs.onToggle = onToggle
}
