package stream.playhuddle.huddle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stream.playhuddle.huddle.data.HuddlePreferencesDataSource
import stream.playhuddle.huddle.ui.NavGraphs
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    huddlePreferencesDataSource: HuddlePreferencesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isAuthenticated = huddlePreferencesDataSource.userData.first().username.isNotEmpty()
            _uiState.update {
                it.copy(
                    startingGraph = if (isAuthenticated) NavGraphs.home else NavGraphs.auth,
                    loading = false
                )
            }
        }
    }
}

data class MainUiState(
    val startingGraph: NavGraphSpec = NavGraphs.home,
    val loading: Boolean = true
)