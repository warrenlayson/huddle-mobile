package stream.playhuddle.huddle

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavGraphSpec
import stream.playhuddle.huddle.ui.NavGraphs
import stream.playhuddle.huddle.ui.composables.BottomBarScreen
import stream.playhuddle.huddle.ui.composables.HuddleBottomBar
import stream.playhuddle.huddle.ui.composables.HuddleScaffold
import stream.playhuddle.huddle.ui.composables.HuddleToolbar
import stream.playhuddle.huddle.ui.destinations.Destination
import stream.playhuddle.huddle.ui.destinations.UserProfileScreenDestination
import stream.playhuddle.huddle.ui.theme.HuddleTheme

@Composable
fun HuddleApp(startingGraph: NavGraphSpec) {
    val viewModel: MainViewModel = hiltViewModel()
    HuddleTheme(dynamicColor = false, darkTheme = false) {
        val navController = rememberNavController()

        val snackbarHostState = remember { SnackbarHostState() }
        HuddleScaffold(
            startRoute = startingGraph,
            navController = navController,
            snackbarHostState = snackbarHostState,
            topBar = {
                HuddleToolbar(
                    visible = it.shouldShowScaffoldElements,
                    onSignOut = viewModel::onSignOut,
                    onDeleteProfile = viewModel::deleteProfile
                )
            },
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
                modifier = Modifier.padding(it),
                dependenciesContainerBuilder = {
                    dependency(snackbarHostState)
                }
            )
        }
    }
}

private val Destination.shouldShowScaffoldElements
    get() = this in BottomBarScreen.values().map { it.direction }


