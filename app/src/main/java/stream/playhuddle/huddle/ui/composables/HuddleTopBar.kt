package stream.playhuddle.huddle.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import stream.playhuddle.huddle.R

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
