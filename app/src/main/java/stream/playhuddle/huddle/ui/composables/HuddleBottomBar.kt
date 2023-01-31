package stream.playhuddle.huddle.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.ui.NavGraphs
import stream.playhuddle.huddle.ui.destinations.DirectionDestination
import stream.playhuddle.huddle.ui.destinations.HomeRouteDestination
import stream.playhuddle.huddle.ui.destinations.InboxRouteDestination
import stream.playhuddle.huddle.ui.destinations.LikesRouteDestination
import stream.playhuddle.huddle.ui.destinations.ProfileRouteDestination
import stream.playhuddle.huddle.ui.theme.md_theme_dark_secondary
import stream.playhuddle.huddle.ui.theme.md_theme_light_primary

@Composable
fun HuddleBottomBar(
    visible: Boolean,
    navController: NavHostController,
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
                    val isCurrentDestOnBackStack =
                        navController.isRouteOnBackStack(screen.direction)
                    NavigationBarItem(
                        selected = isCurrentDestOnBackStack,
                        onClick = {
                            if (isCurrentDestOnBackStack) {
                                navController.popBackStack(screen.direction, false)
                                return@NavigationBarItem
                            }

                            navController.navigate(screen.direction) {
                                popUpTo(NavGraphs.home.route) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }

                        },
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

enum class BottomBarScreen(val direction: DirectionDestination, @DrawableRes val iconRes: Int) {
    Home(iconRes = R.drawable.ic_home_24, direction = HomeRouteDestination),
    Likes(iconRes = R.drawable.heart_icon, direction = LikesRouteDestination),
    Inbox(iconRes = R.drawable.message_icon, direction = InboxRouteDestination),
    Profile(iconRes = R.drawable.profile_icon, direction = ProfileRouteDestination),
}

