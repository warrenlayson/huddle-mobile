package stream.playhuddle.huddle.ui.signup

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import stream.playhuddle.huddle.utils.AuthNavGraph

@AuthNavGraph(start = true)
@Destination
@Composable
fun SignUpRoute() {
    SignUpScreen()
}

@Composable
private fun SignUpScreen() {

}