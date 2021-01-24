package pages

import api.HackerNewsApi
import api.vo.HackerNewsItem
import app.HackerNewsApp
import components.NewsDiscussion
import components.TopBar
import kotlinx.coroutines.launch
import react.*
import taro.Taro
import taro.components.view

external interface DiscussionState : RState {
    var item: HackerNewsItem?
}

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
class Discussion : RComponent<RProps, DiscussionState>() {
    private val api = HackerNewsApi()

    override fun DiscussionState.init() {
        item = null
    }

    override fun componentDidMount() {
        val current = Taro.getCurrentInstance()
        js("console.dir(current.router.params)")
        val id = current.router?.params["id"] ?: return
        HackerNewsApp.scope.launch {
            try {
                val item = api.item((id as String).toInt())
                setState {
                    this.item = item
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun RBuilder.render() {
        view {
            child(TopBar::class) {}
            if (state.item != null) {
                child(NewsDiscussion::class) {
                    attrs {
                        item = state.item!!
                    }
                }
            }
        }
    }
}


