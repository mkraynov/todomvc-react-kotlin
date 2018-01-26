package index

import components.app
import kotlinext.js.Object
import kotlinext.js.invoke
import kotlinext.js.require
import model.TodosModel
import react.dom.*
import kotlin.browser.document

@JsModule("classnames")
external fun classNames(obj: Any): String

enum class NowShowing { ACTIVE_TODOS, ALL_TODOS, COMPLETED_TODOS }

val ENTER_KEYCODE = 13
val ESCAPE_KEYCODE = 27

fun main(args: Array<String>) {
    require("todomvc-common/base.css")
    require("todomvc-app-css/index.css")

    val model = TodosModel()

    fun render() {
        render(document.getElementById("root")) {
            section("todoapp") {
                app(model)
            }
            footer(classes = "info") {
                p { +"Double-click to edit a todo" }
                p {
                    +"Created by "
                    a("http://github.com/mkraynov/") {
                        +"mkraynov"
                    }
                }
            }
        }
    }

    model.subscribe { render() }
    render()
}
