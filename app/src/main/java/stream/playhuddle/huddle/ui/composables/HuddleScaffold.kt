package stream.playhuddle.huddle.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.spec.Route
import stream.playhuddle.huddle.ui.appCurrentDestinationAsState
import stream.playhuddle.huddle.ui.destinations.Destination
import stream.playhuddle.huddle.ui.startAppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HuddleScaffold(
    startRoute: Route,
    navController: NavHostController,
    topBar: @Composable (Destination) -> Unit,
    bottomBar: @Composable (Destination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val destination =
        navController.appCurrentDestinationAsState().value ?: startRoute.startAppDestination

    navController.currentBackStack.collectAsState().value.print()
    Scaffold(
        topBar = { topBar(destination) },
        bottomBar = { bottomBar(destination) },
        content = content
    )
}

private fun List<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = map { it.destination.route }.toTypedArray().contentToString()
    println("$prefix = $stack")
}