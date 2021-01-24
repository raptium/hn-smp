package common

import kotlinx.css.CSSBuilder
import react.RElementBuilder
import taro.components.StandardProps

fun css(builder: CSSBuilder.() -> Unit): String = CSSBuilder().apply(builder).toString()

fun <R : StandardProps<Any>> RElementBuilder<R>.style(builder: CSSBuilder.() -> Unit) {
    attrs {
        style = css(builder)
    }
}
