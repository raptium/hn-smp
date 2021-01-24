package components

import common.Colors
import common.style
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import taro.components.text
import taro.components.view

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
class Loading : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        view {
            style {
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.stretch
                alignContent = Align.center
                justifyContent = JustifyContent.center
                backgroundColor = Colors.bg
                flex(1.0, 1.0, 100.pct)
            }
            text {
                style {
                    color = Colors.altTextColor
                    fontSize = 12.pt
                    textAlign = TextAlign.center
                }
                +"Loading..."
            }
        }
    }

}
