package components

import common.Colors
import common.style
import kotlinx.css.FontWeight
import kotlinx.css.backgroundColor
import kotlinx.css.fontWeight
import kotlinx.css.padding
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import taro.components.text
import taro.components.view

external interface TopBarProps : RProps {

}

@Suppress("NON_EXPORTABLE_TYPE")
@JsExport
@ExperimentalJsExport
class TopBar : RComponent<TopBarProps, RState>() {
    override fun RBuilder.render() {
        view {
            style {
                backgroundColor = Colors.hn
                padding = "10px"
            }
            text {
                style {
                    fontWeight = FontWeight.bold
                }
                +"Hacker News"
            }
        }
    }
}
