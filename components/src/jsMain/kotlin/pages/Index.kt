package pages

import common.style
import components.NewsList
import components.TopBar
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import taro.components.text
import taro.components.view

external interface IndexState : RState {

}

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
class Index : RComponent<RProps, IndexState>() {
    override fun RBuilder.render() {
        view {
            style {
                display = Display.flex
                flexDirection = FlexDirection.column
            }
            child(TopBar::class) {}
            child(NewsList::class) {
                attrs.pageSize = 30
            }
        }
    }
}
