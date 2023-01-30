package stream.playhuddle.huddle.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import stream.playhuddle.huddle.R

@Composable
fun LogoWithText(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.huddle_logo_with_word),
        contentDescription = "Logo",
        modifier = modifier
    )
}