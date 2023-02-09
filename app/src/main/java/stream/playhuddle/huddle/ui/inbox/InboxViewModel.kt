package stream.playhuddle.huddle.ui.inbox

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import stream.playhuddle.huddle.data.Message
import stream.playhuddle.huddle.data.MessagesRepository
import stream.playhuddle.huddle.data.Profile
import stream.playhuddle.huddle.data.UserRepository
import stream.playhuddle.huddle.utils.Result
import stream.playhuddle.huddle.utils.asResult
import timber.log.Timber
import javax.inject.Inject

const val PAU_UID = "pau"

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    userRepository: UserRepository
) : ViewModel() {

    val uiState: StateFlow<InboxUiState> =
        combine(userRepository.getProfile(), messagesRepository.messages, ::Pair)
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> {
                        Timber.e(result.exception)
                        InboxUiState.Error
                    }
                    Result.Loading -> {
                        InboxUiState.Loading
                    }
                    is Result.Success -> {
                        val (profile, messages) = result.data
                        Timber.d(messages.toString())
                        InboxUiState.Success(messages = messages, profile = profile)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = InboxUiState.Loading
            )

    fun onEvent(event: InboxEvent) {
        when (event) {
            is InboxEvent.OnImageSend -> onImageSelected(event.uris, event.userId)
            is InboxEvent.OnSendMessage -> onSend(event.message, event.userId)
        }
    }


    private fun onSend(message: String, userId: String) {

        val msg = Message(
            message = message,
            username = userId
        )

        viewModelScope.launch { messagesRepository.sendMessage(msg) }
    }

    private fun onImageSelected(uris: List<Uri>, userId: String) {
        val tmpMessage =
            Message(username = userId, imageUrl = uris.map { LOADING_IMAGE_URL })
        viewModelScope.launch {
            try {
                messagesRepository.sendImageMessage(message = tmpMessage, uris = uris)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    companion object {
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }

}

sealed interface InboxUiState {
    data class Success(
        val messages: List<Message> = emptyList(),
        val profile: Profile? = null,
    ) : InboxUiState

    object Loading : InboxUiState
    object Error : InboxUiState
}
