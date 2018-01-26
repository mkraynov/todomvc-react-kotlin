package components

import index.ENTER_KEYCODE
import index.NowShowing
import index.classNames
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onKeyDownFunction
import model.TodoModel
import model.TodosModel
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*


interface AppProps : RProps {
    var model: TodosModel
}

interface AppState : RState {
    var editing: String?
    var nowShowing: NowShowing
    var newTodo: String
}

val EMPTY_STRING = ""

class App(props: AppProps) : RComponent<AppProps, AppState>(props) {
    override fun AppState.init(props: AppProps) {
        nowShowing = NowShowing.ALL_TODOS
    }

    fun onKeyDown(event: Event) {
        event.preventDefault();

        val value = state.newTodo.trim();
        if (value !== EMPTY_STRING) {
            props.model.addTodo(value);
            setState {
                newTodo = EMPTY_STRING
            }
        }
    }

    fun onChange(event: Event) {
        val target = event.target as HTMLInputElement
        setState { newTodo = target.value }
    }

    fun getTodos(): List<TodoModel> {
        return props.model.todos.filter {
            when (state.nowShowing) {
                NowShowing.ACTIVE_TODOS -> !it.completed
                NowShowing.COMPLETED_TODOS -> it.completed
                else -> true
            }
        }
    }

    fun onSave(todo: TodoModel, text: String) {
        props.model.save(todo, text)
        setState { editing = null }
    }

    override fun RBuilder.render() {
        div {
            header("header") {
                h1 {
                    +"todos"
                }
                input(type = InputType.text, classes = "new-todo", name = "itemText") {
                    key = "itemText"

                    attrs {
                        value = state.newTodo
                        placeholder = "What needs to be done?"
                        autoFocus = true
                        onChangeFunction = { onChange(it) }
                        onKeyDownFunction = {
                            val keyCode: Int = it.asDynamic().keyCode
                            if (keyCode == ENTER_KEYCODE) {
                                onKeyDown(it)
                            }
                        }
                    }
                }
            }

            if (props.model.todos.size > 0) {
                section("main") {
                    input(type = InputType.checkBox, classes = "toggle-all") {
                        attrs {
                            onChangeFunction = {}
                        }
                    }
                    ul("todo-list") {
                        for (todo in getTodos()) {
                            val obj = object {
                                val completed = todo.completed
                                val editing = state.editing == todo.id
                            }
                            li(classes = classNames(obj)) {
                                key = todo.id

                                todoItem(
                                        todo = todo,
                                        editing = obj.editing,
                                        onCancel = { setState { editing = null } },
                                        onDestroy = { todo: TodoModel -> props.model.destroy(todo) },
                                        onEdit = { todo: TodoModel -> setState { editing = todo.id } },
                                        onSave = { todo: TodoModel, text: String -> onSave(todo, text) },
                                        onToggle = { todo: TodoModel -> props.model.toggle(todo.id) }
                                )
                            }
                        }
                    }
                }
            }
            footer(completedCount = props.model.getCompletedCount(),
                   count = getTodos().size,
                   nowShowing = state.nowShowing,
                   onChangeSelection = { now: NowShowing -> setState { nowShowing = now } },
                   onClearCompleted = { props.model.clearCompleted() }
            )
        }
    }
}

fun RBuilder.app(model: TodosModel) = child(App::class) {
    attrs.model = model
}
