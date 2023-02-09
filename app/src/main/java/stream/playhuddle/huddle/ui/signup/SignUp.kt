package stream.playhuddle.huddle.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.ui.composables.Banner
import stream.playhuddle.huddle.ui.composables.LogoWithText
import stream.playhuddle.huddle.ui.destinations.HomeRouteDestination
import stream.playhuddle.huddle.ui.theme.HuddleTheme
import stream.playhuddle.huddle.utils.AuthNavGraph

@AuthNavGraph
@Destination
@Composable
fun SignUpRoute(navigator: DestinationsNavigator, snackbarHostState: SnackbarHostState) {
    val viewModel: SignUpViewModel = hiltViewModel()
    SignUpScreen(
        uiState = viewModel.uiState,
        onEvent = viewModel::onEvent,
        onNavigateToHome = { navigator.navigate(HomeRouteDestination) },
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun SignUpScreen(
    uiState: SignUpUiState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onEvent: (SignUpEvent) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {

    if (uiState.errorMessage != null) {
        LaunchedEffect(snackbarHostState, uiState.errorMessage) {
            snackbarHostState.showSnackbar(uiState.errorMessage)
        }
    }

    if (uiState.showBanner) {
        Dialog(onDismissRequest = { onEvent(SignUpEvent.ShowDialog(false)) }) {
            Banner(
                name = uiState.username,
                onClick = { onEvent(SignUpEvent.StartSwiping) },
                modifier = Modifier.heightIn(min = 400.dp)
            )
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

        Text(
            text = "Create you profile below \n and start swiping!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )

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

                val ageFocusRequester = remember { FocusRequester() }
                val locationRequester = remember { FocusRequester() }
                val interestsRequester = remember { FocusRequester() }
                val bioFocusRequester = remember { FocusRequester() }
                val emailFocusRequester = remember { FocusRequester() }
                val passwordRequester = remember { FocusRequester() }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.username_label))
                    RoundedOutlineTextField(
                        value = uiState.username,
                        onValueChange = { onEvent(SignUpEvent.OnUsernameChange(it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                        ),
                        onImeAction = { emailFocusRequester.requestFocus() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.email_label))
                    RoundedOutlineTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(SignUpEvent.OnEmailChange(it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        onImeAction = { passwordRequester.requestFocus() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(emailFocusRequester),
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.password_label))
                    var obscured by remember { mutableStateOf(true)}
                    RoundedOutlineTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(SignUpEvent.OnPasswordChange(it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Password
                        ),
                        onImeAction = { ageFocusRequester.requestFocus() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordRequester),
                        visualTransformation = if (obscured) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            if (obscured) {
                                IconButton(onClick = {obscured = false}) {
                                    Icon(painter = painterResource(id = R.drawable.baseline_visibility_24), contentDescription = null)
                                }
                            } else {
                                IconButton(onClick = {obscured = true}) {
                                    Icon(painter = painterResource(id = R.drawable.baseline_visibility_off_24), contentDescription = null)
                                }
                            }
                        }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = stringResource(R.string.age_label))
                        RoundedOutlineTextField(
                            value = uiState.age,
                            onValueChange = { onEvent(SignUpEvent.OnAgeChange(it)) },
                            modifier = Modifier
                                .width(100.dp)
                                .focusRequester(ageFocusRequester),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            ),
                            onImeAction = { locationRequester.requestFocus() }
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = stringResource(R.string.location_label))
                        RoundedOutlineTextField(
                            value = uiState.location,
                            onValueChange = { onEvent(SignUpEvent.OnLocationChange(it)) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                            ),
                            onImeAction = { interestsRequester.requestFocus() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(locationRequester)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.interests_label))
                    RoundedOutlineTextField(
                        value = uiState.interests,
                        onValueChange = { onEvent(SignUpEvent.OnInterestsChange(it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                        ),
                        onImeAction = { bioFocusRequester.requestFocus() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(interestsRequester)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.bio_label))
                    RoundedOutlineTextField(
                        value = uiState.bio,
                        onValueChange = { onEvent(SignUpEvent.OnBioChange(it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                        ),
                        onImeAction = { onEvent(SignUpEvent.OnSave) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(bioFocusRequester)
                    )
                }

                Button(
                    onClick = { onEvent(SignUpEvent.OnSave) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(R.string.create_profile),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedOutlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onImeAction: KeyboardActionScope.() -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable () -> Unit = {},
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
        ),
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = onImeAction),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    HuddleTheme {
        SignUpScreen(uiState = SignUpUiState())
    }
}