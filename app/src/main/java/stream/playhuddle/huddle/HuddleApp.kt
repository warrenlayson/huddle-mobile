package stream.playhuddle.huddle

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.NavGraphSpec
import stream.playhuddle.huddle.ui.NavGraphs
import stream.playhuddle.huddle.ui.composables.BottomBarScreen
import stream.playhuddle.huddle.ui.composables.HuddleBottomBar
import stream.playhuddle.huddle.ui.composables.HuddleScaffold
import stream.playhuddle.huddle.ui.composables.HuddleToolbar
import stream.playhuddle.huddle.ui.destinations.Destination
import stream.playhuddle.huddle.ui.theme.HuddleTheme

@Composable
fun HuddleApp(startingGraph: NavGraphSpec) {
    HuddleTheme(dynamicColor = false, darkTheme = false) {
        val navController = rememberNavController()

        HuddleScaffold(
            startRoute = startingGraph,
            navController = navController,
            topBar = { HuddleToolbar(it.shouldShowScaffoldElements) },
            bottomBar = {
                HuddleBottomBar(
                    visible = it.shouldShowScaffoldElements,
                    navController = navController
                )
            }
        ) {
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.root,
                startRoute = startingGraph,
                modifier = Modifier.padding(it)
            )
        }
    }
}

private val Destination.shouldShowScaffoldElements
    get() = this in BottomBarScreen.values().map { it.direction }


