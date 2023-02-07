package stream.playhuddle.huddle.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import stream.playhuddle.huddle.data.UserRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> uiState = uiState.copy(email = event.email)
            LoginEvent.OnLogin -> login()
            is LoginEvent.OnPasswordChange -> uiState = uiState.copy(password = event.password)
            LoginEvent.ClearMessage -> uiState = uiState.copy(errorMessage = null)
        }
    }

    private fun login() {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                userRepository.signIn(uiState.email, uiState.password)
            } catch (e: FirebaseAuthException) {
                if (e.errorCode == "ERROR_USER_NOT_FOUND") {
                    uiState = uiState.copy(errorMessage = "User not found")
                }
            }
        }
        uiState = uiState.copy(isLoading = false)
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)