package app

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object HackerNewsApp {
    val scope = MainScope()

    fun onEnter() {
    }

    fun onExit() {
    }
}

fun launch(block: suspend () -> Unit) {
    HackerNewsApp.scope.launch {
        block()
    }
}
