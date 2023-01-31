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
        User(
            username = it.username,
            age = it.age,
            location = it.location,
            interests = it.interests,
            bio = it.bio,
            onboarded = it.onboarded
        )
    }

    suspend fun setProfile(
        username: String,
        age: Int,
        location: String,
        bio: String,
        interests: String
    ) = userPreferencesDatasource.updateData {
        it.copy {
            this.username = username
            this.age = age
            this.location = location
            this.bio = bio
            this.interests = interests
        }
    }

    suspend fun toggleOnboarded(value: Boolean) =
        userPreferencesDatasource.updateData { it.copy { this.onboarded = value } }
}