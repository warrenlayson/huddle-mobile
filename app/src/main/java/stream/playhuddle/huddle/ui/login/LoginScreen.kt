package stream.playhuddle.huddle.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.ui.composables.LogoWithText
import stream.playhuddle.huddle.ui.signup.RoundedOutlineTextField
import stream.playhuddle.huddle.ui.theme.HuddleTheme
import stream.playhuddle.huddle.utils.AuthNavGraph

@AuthNavGraph
@Destination
@Composable
fun LoginScreen(snackbarHostState: SnackbarHostState) {

    val viewModel: LoginViewModel = hiltViewModel()
    LoginScreen(
        uiState = viewModel.uiState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    onEvent: (LoginEvent) -> Unit = {},
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {

    if (uiState.errorMessage != null) {
        LaunchedEffect(snackbarHostState, uiState.errorMessage) {
            snackbarHostState.showSnackbar(uiState.errorMessage)
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xffEAC3CE))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoWithText(modifier = Modifier.padding(top = 40.dp))
        Card(
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(0xfff07ba0)
            ),
            modifier = Modifier.padding(24.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val passwordRequester = remember { FocusRequester() }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.email_label))
                    RoundedOutlineTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(LoginEvent.OnEmailChange(it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                        ),
                        onImeAction = { passwordRequester.requestFocus() },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.password_label))
                    val keyboardController = LocalSoftwareKeyboardController.current
                    RoundedOutlineTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(LoginEvent.OnPasswordChange(it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                        ),
                        onImeAction = {
                            onEvent(LoginEvent.OnLogin)
                            keyboardController?.hide()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordRequester),
                    )
                }

                Button(
                    onClick = { onEvent(LoginEvent.OnLogin) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    HuddleTheme {
        LoginScreen(uiState = LoginUiState())
    }
}
