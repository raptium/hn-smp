package components

import api.vo.HackerNewsItem
import app.launch
import common.Colors
import common.style
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import taro.Taro
import taro.components.text
import taro.components.view

external interface NewsDiscussionProps : RProps {
    var item: HackerNewsItem
}

external interface NewsDiscussionState : RState {

}

@Suppress("NON_EXPORTABLE_TYPE")
@JsExport
class NewsDiscussion(props: NewsDiscussionProps) : RComponent<NewsDiscussionProps, NewsDiscussionState>(props) {
    override fun RBuilder.render() {
        val item = props.item
        view {
            style {
                backgroundColor = Colors.bg
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.stretch
                flex(1.0, 1.0, 100.pct)
                padding = "10px"
            }
            text {
                +item.title
            }
            if (item.url != null) {
                view {
                    text {
                        style {
                            color = Colors.altTextColor
                            fontSize = 9.pt
                        }
                        attrs {
                            onClick = {
                                launch { Taro.setClipboardData(item.url) }
                            }
                        }
                        +item.url
                    }
                }
            }
        }
    }
}
