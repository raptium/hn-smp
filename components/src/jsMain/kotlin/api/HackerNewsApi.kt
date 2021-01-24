package api

import api.vo.HackerNewsItem
import common.HttpClient

class HackerNewsApi {
    private val httpClient = HttpClient()
    private val base = "https://hacker-news.firebaseio.com/v0"

    suspend fun item(id: Int): HackerNewsItem {
        val result: HttpClient.Response<HackerNewsItem> = httpClient.get("$base/item/$id.json")
        if (result.status == 200) {
            return result.data!!
        } else {
            throw RuntimeException("Failed to fetch item, status: ${result.status}")
        }
    }

    suspend fun newStories(): List<Int> = stories("newstories")
    suspend fun topStories(): List<Int> = stories("topstories")

    private suspend fun stories(path: String): List<Int> {
        val result: HttpClient.Response<List<Int>> = httpClient.get("$base/$path.json")
        if (result.status == 200) {
            return result.data!!
        } else {
            throw RuntimeException("Failed to fetch stories, status: ${result.status}")
        }
    }
}
