package stream.playhuddle.huddle

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import stream.playhuddle.huddle.ui.NavGraphs
import stream.playhuddle.huddle.ui.appCurrentDestinationAsState
import stream.playhuddle.huddle.ui.destinations.Destination
import stream.playhuddle.huddle.ui.destinations.HomeRouteDestination
import stream.playhuddle.huddle.ui.destinations.InboxRouteDestination
import stream.playhuddle.huddle.ui.destinations.LikesRouteDestination
import stream.playhuddle.huddle.ui.destinations.ProfileRouteDestination
import stream.playhuddle.huddle.ui.startAppDestination
import stream.playhuddle.huddle.ui.theme.HuddleTheme
import stream.playhuddle.huddle.ui.theme.md_theme_dark_primary
import stream.playhuddle.huddle.ui.theme.md_theme_dark_secondary
import stream.playhuddle.huddle.ui.theme.md_theme_light_primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HuddleApp() {
    HuddleTheme(dynamicColor = false, darkTheme = false) {
        val navController = rememberNavController()
        val currentDestination: Destination =
            navController.appCurrentDestinationAsState().value ?: NavGraphs.home.startAppDestination
        var topBarState by remember { mutableStateOf(false) }
        var bottomBarState by remember { mutableStateOf(false) }

        when (currentDestination) {
            in NavGraphs.home.destinations -> {
                topBarState = true
                bottomBarState = true
            }
            else -> {
                bottomBarState = false
                topBarState = false
            }
        }

        Scaffold(
            topBar = { HuddleToolbar(visible = topBarState) },
            bottomBar = {
                HuddleBottomBar(
                    visible = bottomBarState,
                    destination = currentDestination,
                    onIconClick = {
                        navController.navigate(it.direction) {
                            popUpTo(NavGraphs.home.route) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            },
        ) { innerPadding ->
            DestinationsNavHost(
                navGraph = NavGraphs.auth,
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun HuddleToolbar(visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        Surface(
            shadowElevation = 3.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.huddle_logo_with_word_red),
                    contentDescription = null,
                    modifier = Modifier.height(80.dp)
                )
                IconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(end = 8.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.burger_menu),
                        contentDescription = "Menu",
                        tint = Red,
                    )
                }
            }
        }
    }
}

enum class BottomBarScreen(val direction: DirectionDestinationSpec, @DrawableRes val iconRes: Int) {
    Home(iconRes = R.drawable.ic_home_24, direction = HomeRouteDestination),
    Likes(iconRes = R.drawable.heart_icon, direction = LikesRouteDestination),
    Inbox(iconRes = R.drawable.message_icon, direction = InboxRouteDestination),
    Profile(iconRes = R.drawable.profile_icon, direction = ProfileRouteDestination),
}

@Composable
fun HuddleBottomBar(
    visible: Boolean,
    destination: Destination,
    onIconClick: (BottomBarScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Surface(shadowElevation = 3.dp) {
            NavigationBar(modifier = modifier, containerColor = Color.White) {
                BottomBarScreen.values().forEach { screen ->
                    val selected = destination == screen.direction
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onIconClick(screen) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = md_theme_dark_secondary,
                            unselectedIconColor = md_theme_light_primary,
                            indicatorColor = Color.White
                        ),
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.iconRes),
                                contentDescription = screen.name,
                            )
                        },
                    )
                }

            }
        }
    }
}
