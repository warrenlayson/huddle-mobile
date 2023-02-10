package stream.playhuddle.huddle.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import stream.playhuddle.huddle.data.HuddlePreferencesDataSource
import stream.playhuddle.huddle.data.Profile
import stream.playhuddle.huddle.data.UserRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val huddlePreferencesDataSource: HuddlePreferencesDataSource,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(SignUpUiState())
        private set

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnAgeChange -> uiState = uiState.copy(age = event.value)
            is SignUpEvent.OnBioChange -> uiState = uiState.copy(bio = event.value)
            is SignUpEvent.OnInterestsChange -> uiState = uiState.copy(interests = event.value)
            is SignUpEvent.OnLocationChange -> uiState = uiState.copy(location = event.value)
            is SignUpEvent.OnUsernameChange -> uiState = uiState.copy(username = event.value)
            is SignUpEvent.ShowDialog -> showDialog(event.value)
            SignUpEvent.OnSave -> {
                uiState = uiState.copy(isLoading = true)
                viewModelScope.launch {
                    uiState.run {
                        val profile = Profile(
                            username = username,
                            age = age.toInt(),
                            location = location,
                            interests = interests,
                            bio = bio,
                        )

                        try {
                            userRepository.signUp(email, password, profile)
                            showDialog(true)
                        } catch (e: FirebaseAuthException) {
                            Timber.e(e.errorCode)
                            if (e.errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                                uiState = uiState.copy(errorMessage = "Email is already in use")
                            }
                            uiState = uiState.copy(errorMessage = "Unknown error occurred")
                        }
                    }
                }
                uiState = uiState.copy(isLoading = false)

            }
            SignUpEvent.StartSwiping -> viewModelScope.launch {
                huddlePreferencesDataSource.toggleOnboarded(
                    true
                )
            }
            is SignUpEvent.OnEmailChange -> uiState = uiState.copy(email = event.value)
            is SignUpEvent.OnPasswordChange -> uiState = uiState.copy(password = event.value)
        }
    }

    private fun showDialog(value: Boolean) {
        uiState = uiState.copy(showBanner = value)
    }

}

data class SignUpUiState(
    val username: String = "",
    val age: String = "",
    val location: String = "",
    val interests: String = "",
    val bio: String = "",
    val email: String = "",
    val password: String = "",
    val showBanner: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)