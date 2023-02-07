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
import stream.playhuddle.huddle.data.HuddlePreferencesDataSource
import stream.playhuddle.huddle.data.UserRepository
import stream.playhuddle.huddle.ui.NavGraphs
import stream.playhuddle.huddle.utils.Result
import stream.playhuddle.huddle.utils.asResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    huddlePreferencesDataSource: HuddlePreferencesDataSource,
    userRepository: UserRepository
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
                        val authenticated = user.id.isNotEmpty() && onboarded
                        Timber.d(onboarded.toString())
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

}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val startingGraph: NavGraphSpec = NavGraphs.home) : MainUiState
}
