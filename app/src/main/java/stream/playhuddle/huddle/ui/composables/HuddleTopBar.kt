package stream.playhuddle.huddle.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import stream.playhuddle.huddle.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HuddleToolbar(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {},
    onDeleteProfile: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })

    ) {
        TopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.huddle_logo_with_word_red),
                    contentDescription = null,
                    modifier = Modifier.height(80.dp)
                )
            },
            actions = {
                var open by remember { mutableStateOf(false) }
                IconButton(onClick = { open = !open }, modifier = Modifier.padding(end = 8.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.burger_menu),
                        contentDescription = "Menu",
                        tint = Red,
                    )
                }
                DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                    DropdownMenuItem(text = { Text(text = "Sign Out") }, onClick = onSignOut)
                    DropdownMenuItem(
                        text = { Text(text = "Delete Profile") },
                        onClick = onDeleteProfile
                    )
                }
            }
        )
    }
}
