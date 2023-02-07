package stream.playhuddle.huddle.ui.welcome

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.ui.composables.LogoWithText
import stream.playhuddle.huddle.ui.destinations.LoginScreenDestination
import stream.playhuddle.huddle.ui.destinations.SignUpRouteDestination
import stream.playhuddle.huddle.ui.theme.md_theme_dark_secondary
import stream.playhuddle.huddle.ui.theme.md_theme_dark_tertiary
import stream.playhuddle.huddle.utils.AuthNavGraph

@AuthNavGraph(start = true)
@Destination
@Composable
fun WelcomeRoute(navigator: DestinationsNavigator) {
    WelcomeScreen(
        navigateToSignUp = { navigator.navigate(SignUpRouteDestination) },
        navigateToLogin = { navigator.navigate(LoginScreenDestination) }
    )
}

@Composable
private fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navigateToSignUp: () -> Unit = {},
    navigateToLogin: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(factory = {
            ImageView(it).apply {
                setImageResource(R.drawable.background)
                scaleType = ImageView.ScaleType.CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        })

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            LogoWithText(
                modifier = Modifier.padding(top = 80.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AuthButton(
                    onClick = navigateToLogin,
                    containerColor = md_theme_dark_secondary,
                ) {
                    Text(
                        text = "Log In",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                    )
                }
                AuthButton(
                    containerColor = md_theme_dark_tertiary,
                    onClick = navigateToSignUp
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                    )
                }
            }


            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Need help?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }

        }
    }
}

@Composable
fun AuthButton(
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.Black
        ),
        modifier = modifier.width(150.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp
        ),
        content = content
    )
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}