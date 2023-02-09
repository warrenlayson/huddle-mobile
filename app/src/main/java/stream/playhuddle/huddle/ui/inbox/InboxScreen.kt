package stream.playhuddle.huddle.ui.inbox

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.data.Message
import stream.playhuddle.huddle.ui.theme.HuddleTheme
import stream.playhuddle.huddle.utils.HomeNavGraph

@HomeNavGraph
@Destination
@Composable
fun InboxRoute() {
    val viewModel = hiltViewModel<InboxViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InboxScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun InboxScreen(
    uiState: InboxUiState,
    onEvent: (InboxEvent) -> Unit = {}
) {
    when (uiState) {
        InboxUiState.Error -> Box(modifier = Modifier, contentAlignment = Alignment.Center) {
            Text(text = "Error")
        }
        InboxUiState.Loading -> {
            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is InboxUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xfff7f7f7))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = buildAnnotatedString {
                    append("You're chatting with ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Its_me_Paulo_27")
                    }
                }, textAlign = TextAlign.Center)
                LazyColumn(
                    modifier = Modifier.weight(1f, true),
                    reverseLayout = true,
                    content = {
                        items(uiState.messages.asReversed()) { message ->
                            MessageBox(message = message)
                        }
                    },
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val galleryLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetMultipleContents(),
                        onResult = { uriList ->
                            if (uriList.isNotEmpty()) {
                                onEvent(
                                    InboxEvent.OnImageSend(
                                        uriList,
                                        uiState.profile?.username.orEmpty()
                                    )
                                )
                            }
                        }
                    )
                    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.gallery_button),
                            contentDescription = "Open gallery",
                            tint = Color(0xffd93542)
                        )
                    }
                    var message by remember { mutableStateOf("") }
                    val onMessageSend = {
                        onEvent(
                            InboxEvent.OnSendMessage(
                                message = message,
                                userId = uiState.profile?.username.orEmpty()
                            )
                        )
                        message = ""
                    }
                    MessageTextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier.weight(1f, true),
                        onImeAction = onMessageSend
                    )
                    IconButton(onClick = onMessageSend) {
                        Icon(
                            painter = painterResource(id = R.drawable.send_button),
                            contentDescription = "Send",
                            tint = Color(0xffd93542)
                        )
                    }
                }

            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MessageBox(
    message: Message,
) {
    val isPaulo = message.username == PAU_UID
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when {
            isPaulo -> Alignment.Start
            else -> Alignment.End
        }
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.Bottom
        ) {
            if (isPaulo) {
                Image(
                    painter = painterResource(R.drawable.paulo_pfp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(
                            CircleShape
                        )
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Card(
                modifier = Modifier.widthIn(max = 340.dp),
                shape = cardShapeFor(message = message),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isPaulo -> Color(0xffD93542)
                        else -> Color(0xff3D3A3A)
                    }
                )
            ) {
                message.message?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier.padding(8.dp),
                        color = Color.White
                    )
                }

                message.imageUrl?.let { imageUrls ->
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        imageUrls.forEach { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            if (!isPaulo) {
                Image(
                    painter = painterResource(R.drawable.user_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(
                            CircleShape
                        )
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun cardShapeFor(message: Message): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when (message.username) {
        PAU_UID -> roundedCorners.copy(bottomStart = CornerSize(0))
        else -> roundedCorners.copy(bottomEnd = CornerSize(0))
    }

}

@Preview
@Composable
fun MessageBoxPreview() {
    HuddleTheme {
        MessageBox(
            message = Message(message = "Hi! Nice to meet you!", username = "MusicLover555")
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MessageTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Send,
        ),
        keyboardActions = KeyboardActions(
            onSend = { onImeAction() },
        )
    ) { innerTextField ->
        TextFieldDefaults.OutlinedTextFieldDecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(8.dp),
            container = {
                TextFieldDefaults.OutlinedBorderContainerBox(
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = TextFieldDefaults.outlinedTextFieldColors(),
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InboxScreenPreview() {
    HuddleTheme {
        InboxScreen(
            uiState = InboxUiState.Success(
                messages = emptyList(),
            )
        )
    }
}