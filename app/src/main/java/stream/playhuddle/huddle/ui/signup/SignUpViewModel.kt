package stream.playhuddle.huddle.ui.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnAgeChange -> _uiState.update { it.copy(age = event.value) }
            is SignUpEvent.OnBioChange -> _uiState.update { it.copy(bio = event.value) }
            is SignUpEvent.OnInterestsChange -> _uiState.update { it.copy(interests = event.value) }
            is SignUpEvent.OnLocationChange -> _uiState.update { it.copy(location = event.value) }
            is SignUpEvent.OnUsernameChange -> _uiState.update { it.copy(username = event.value) }
            is SignUpEvent.ShowDialog -> _uiState.update { it.copy(showBanner = event.value) }
            SignUpEvent.OnSave -> {}
        }
    }

}

data class SignUpUiState(
    val username: String = "",
    val age: String = "",
    val location: String = "",
    val interests: String = "",
    val bio: String = "",
    val showBanner: Boolean = false
)