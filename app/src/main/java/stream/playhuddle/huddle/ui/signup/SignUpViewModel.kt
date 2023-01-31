package stream.playhuddle.huddle.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stream.playhuddle.huddle.data.HuddlePreferencesDataSource
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val huddlePreferencesDataSource: HuddlePreferencesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnAgeChange -> _uiState.update { it.copy(age = event.value) }
            is SignUpEvent.OnBioChange -> _uiState.update { it.copy(bio = event.value) }
            is SignUpEvent.OnInterestsChange -> _uiState.update { it.copy(interests = event.value) }
            is SignUpEvent.OnLocationChange -> _uiState.update { it.copy(location = event.value) }
            is SignUpEvent.OnUsernameChange -> _uiState.update { it.copy(username = event.value) }
            is SignUpEvent.ShowDialog -> showDialog(event.value)
            SignUpEvent.OnSave -> {
                viewModelScope.launch {
                    uiState.value.run {
                        huddlePreferencesDataSource.setProfile(
                            username = username,
                            age = age.toInt(),
                            location = location,
                            interests = interests,
                            bio = bio,
                        )
                    }
                }

                showDialog(true)
            }
            SignUpEvent.StartSwiping -> viewModelScope.launch {
                huddlePreferencesDataSource.toggleOnboarded(
                    true
                )
            }
        }
    }

    private fun showDialog(value: Boolean) = _uiState.update { it.copy(showBanner = value) }

}

data class SignUpUiState(
    val username: String = "",
    val age: String = "",
    val location: String = "",
    val interests: String = "",
    val bio: String = "",
    val showBanner: Boolean = false
)