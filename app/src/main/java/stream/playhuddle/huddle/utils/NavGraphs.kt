package stream.playhuddle.huddle.utils

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@NavGraph
annotation class HomeNavGraph(
    val start: Boolean = false
)

@NavGraph
annotation class AuthNavGraph(
    val start: Boolean = false
)