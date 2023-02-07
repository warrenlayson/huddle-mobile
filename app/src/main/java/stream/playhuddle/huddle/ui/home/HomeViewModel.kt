package stream.playhuddle.huddle.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import stream.playhuddle.huddle.data.Profile
import stream.playhuddle.huddle.data.User
import stream.playhuddle.huddle.data.UserRepository
import stream.playhuddle.huddle.utils.Result
import stream.playhuddle.huddle.utils.asResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = userRepository.getProfile()
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> {
                    Timber.e(result.exception)
                    HomeUiState.Error
                }
                Result.Loading -> HomeUiState.Loading
                is Result.Success -> HomeUiState.Success(result.data)
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = HomeUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )
}


sealed interface HomeUiState {
    data class Success(val profile: Profile?) : HomeUiState
    object Loading : HomeUiState
    object Error: HomeUiState
}
