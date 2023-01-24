package stream.playhuddle.huddle.ui.home

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import stream.playhuddle.huddle.utils.HomeNavGraph

@HomeNavGraph(start = true)
@Destination
@Composable
fun HomeRoute() {
   HomeScreen()
}

@Composable
private fun HomeScreen() {

}