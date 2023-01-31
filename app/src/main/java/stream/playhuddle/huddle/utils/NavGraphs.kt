package stream.playhuddle.huddle.utils

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@NavGraph
annotation class HomeNavGraph(
    val start: Boolean = false
)

@RootNavGraph
@NavGraph
annotation class AuthNavGraph(
    val start: Boolean = false
)