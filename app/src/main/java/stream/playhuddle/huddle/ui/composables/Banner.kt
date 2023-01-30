package stream.playhuddle.huddle.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.ui.theme.Bebas
import stream.playhuddle.huddle.ui.theme.Glacial
import stream.playhuddle.huddle.ui.theme.HuddleTheme
import stream.playhuddle.huddle.ui.theme.md_theme_dark_secondary

@Composable
fun Banner(name: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xffF07BA0),
                        md_theme_dark_secondary,
                    )
                ),
                shape = MaterialTheme.shapes.large
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_created).uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = Bebas,
                    fontWeight = FontWeight.W600,
                    color = Color(0xff3D3A3A),
                    fontSize = 48.sp
                )
            )

            Text(
                text = stringResource(R.string.welcome_to_huddle),
                fontFamily = Glacial,
                fontSize = 18.sp
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = name,
                    fontFamily = Glacial,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            TextButton(onClick = onClick) {
                Text(
                    text = stringResource(R.string.start_swiping).uppercase(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = Bebas,
                        color = Color(0xff3D3A3A),
                        fontSize = 48.sp
                    )
                )
            }
        }

    }
}

@Preview()
@Composable
private fun BannerPreview() {
    HuddleTheme {
        Banner(name = "MusicLover55", modifier = Modifier.height(500.dp))
    }
}