package components

import api.HackerNewsApi
import api.vo.HackerNewsItem
import app.HackerNewsApp
import app.launch
import common.Colors
import common.style
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import taro.Taro
import taro.components.text
import taro.components.view

external interface NewsListProps : RProps {
    var pageSize: Int?
}

external interface NewsListState : RState {
    var pageSize: Int
    var page: Int
    var items: List<HackerNewsItem>
    var loading: Boolean
}

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
class NewsList(props: NewsListProps) : RComponent<NewsListProps, NewsListState>(props) {
    private val api = HackerNewsApi()

    override fun NewsListState.init(props: NewsListProps) {
        page = 1
        pageSize = props.pageSize ?: 30
        items = emptyList()
        loading = false
    }

    private suspend fun loadStories(): List<HackerNewsItem> {
        val items = api.topStories()
        val skip = (state.page - 1) * state.pageSize
        if (skip >= items.size) {
            return emptyList()
        }
        val pageItemIds = items.subList(skip, (skip + state.pageSize).coerceAtMost(items.size))
        return coroutineScope {
            val tasks = pageItemIds.map {
                async {
                    try {
                        api.item(it)
                    } catch (ex: Exception) {
                        null
                    }
                }
            }
            tasks.awaitAll().filterNotNull()
        }
    }

    override fun componentDidMount() {
        launch {
            try {
                setState { loading = true }
                val newStories = loadStories()
                setState {
                    items = newStories
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                setState {
                    loading = false
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (state.loading) {
            child(Loading::class) {
            }
            return
        }
        var i = 1
        view {
            style {
                backgroundColor = Colors.bg
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.stretch
                flex(1.0, 1.0, 100.pct)
            }
            for (item in state.items) {
                child(NewsListItem::class) {
                    attrs {
                        index = (state.page - 1) * state.pageSize + i
                        this.item = item
                    }
                }
                i += 1
            }
        }
    }
}

external interface NewsListItemProps : RProps {
    var index: Int
    var item: HackerNewsItem
}

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
class NewsListItem : RComponent<NewsListItemProps, RState>() {

    private fun getDomain(): String? {
        val url = props.item.url ?: return null
        val index1 = url.indexOf("://")
        val index2 = url.indexOf("/", index1 + 3)
        return url.substring(index1 + 3, index2)
    }

    override fun RBuilder.render() {
        val domain = getDomain()
        with(props.item) {
            view {
                style {
                    marginTop = 10.px
                    marginLeft = 10.px
                    marginRight = 10.px
                    fontSize = 11.pt
                }
                view {
                    text {
                        attrs {
                            onClick = {
                                launch {
                                    Taro.navigateTo {
                                        url = "/pages/news/discussion?id=${props.item.id}"
                                    }
                                }
                            }
                        }
                        +title
                    }
                    if (domain != null) {
                        text {
                            style {
                                marginLeft = 4.px
                                fontSize = 9.pt
                                color = Color("#828282")
                            }
                            +"($domain)"
                        }
                    }
                }
                view {
                    style {
                        fontSize = 9.pt
                        color = Color("#828282")
                    }
                    text {
                        +"$score point by $by | ${kids?.size ?: 0} comments"
                    }
                }
            }
        }
    }
}
