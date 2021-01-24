package api.vo

import kotlinx.serialization.Serializable

@Serializable
data class HackerNewsItem(
    val id: Int,
    val type: String,
    val by: String,
    val time: Int,
    val title: String,
    val descendants: Int = 0,
    val score: Int = 0,
    val url: String? = null,
    val kids: List<Int>? = null,
)

