package components

import index.NowShowing
import index.classNames
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import utils.pluralize

interface FooterProps: RProps {
    var completedCount: Int
    var count: Int
    var nowShowing: NowShowing
    var onChangeSelection: (nowShowing: NowShowing) -> Unit
    var onClearCompleted: () -> Unit
}

class Footer(props: FooterProps) : RComponent<FooterProps, RState>() {
    override fun RBuilder.render() {
        footer("footer") {
            span("todo-count") {
                strong {
                    +"${props.count} "
                }
                +pluralize(props.count, "item")
                +" left"
            }
            ul("filters") {
                li {
                    a(href = "#/",
                      classes = classNames(object { val selected = (props.nowShowing == NowShowing.ALL_TODOS) })) {
                        attrs {
                            onClickFunction = { props.onChangeSelection(NowShowing.ALL_TODOS) }
                        }
                        +"All"
                    }
                }
                li {
                    +" "
                }
                li {
                    a(href="#/active",
                      classes = classNames(object { val selected = (props.nowShowing == NowShowing.ACTIVE_TODOS) })) {
                        attrs {
                            onClickFunction = { props.onChangeSelection(NowShowing.ACTIVE_TODOS) }
                        }
                        +"Active"
                    }
                }
                li {
                    +" "
                }
                li {
                    a(href="#/completed",
                      classes = classNames(object { val selected = (props.nowShowing == NowShowing.COMPLETED_TODOS) })) {
                        attrs {
                            onClickFunction = { props.onChangeSelection(NowShowing.COMPLETED_TODOS) }
                        }
                        +"Completed"
                    }
                }
            }
            if (props.completedCount > 0) {
                button(classes = "clear-completed") {
                    attrs {
                        onClickFunction = {
                            props.onClearCompleted()
                        }
                    }
                    +"Clear completed"
                }
            }
        }
    }
}

fun RBuilder.footer(completedCount: Int,
                    count: Int,
                    nowShowing: NowShowing,
                    onChangeSelection: (nowShowing: NowShowing) -> Unit,
                    onClearCompleted: () -> Unit
) = child(Footer::class) {
    attrs.completedCount = completedCount
    attrs.count = count
    attrs.nowShowing = nowShowing
    attrs.onChangeSelection = onChangeSelection
    attrs.onClearCompleted = onClearCompleted
}