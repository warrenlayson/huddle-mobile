package stream.playhuddle.huddle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import stream.playhuddle.huddle.data.HuddlePreferencesDataSource
import stream.playhuddle.huddle.data.UserRepository
import stream.playhuddle.huddle.ui.NavGraphs
import stream.playhuddle.huddle.ui.NavGraphs.auth
import stream.playhuddle.huddle.utils.Result
import stream.playhuddle.huddle.utils.asResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    huddlePreferencesDataSource: HuddlePreferencesDataSource,
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState: StateFlow<MainUiState> =
        combine(
            huddlePreferencesDataSource.userData.map { it.onboarded },
            userRepository.currentUser,
            ::Pair
        ).asResult()
            .map { result ->
                when (result) {
                    is Result.Error,
                    Result.Loading -> MainUiState.Loading
                    is Result.Success -> {
                        val (onboarded, user) = result.data
                        Timber.d(user.toString())
                        val authenticated = user.id.isNotEmpty() && onboarded
                        Timber.d(authenticated.toString())
                        Timber.d("onboarded: $onboarded")
                        MainUiState.Success(
                            startingGraph = if (authenticated)
                                NavGraphs.home
                            else NavGraphs.auth
                        )
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState.Loading
            )

    fun onSignOut() = viewModelScope.launch { userRepository.signOut() }

    fun deleteProfile() = viewModelScope.launch { userRepository.deleteAccount() }

}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val startingGraph: NavGraphSpec = NavGraphs.home) : MainUiState
}
