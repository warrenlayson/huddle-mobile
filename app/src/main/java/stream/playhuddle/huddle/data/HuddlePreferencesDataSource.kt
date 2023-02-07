package stream.playhuddle.huddle.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import stream.playhuddle.huddle.UserPreferences
import stream.playhuddle.huddle.copy
import javax.inject.Inject

class HuddlePreferencesDataSource @Inject constructor(
    private val userPreferencesDatasource: DataStore<UserPreferences>
) {

    val userData = userPreferencesDatasource.data.map {
        UserData(
            onboarded = it.onboarded
        )
    }

    suspend fun toggleOnboarded(value: Boolean) =
        userPreferencesDatasource.updateData { it.copy { this.onboarded = value } }
}